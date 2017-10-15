package com.neil.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.neil.core.config.SystemConfig;

public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static final int BUFFER_SIZE = 1024;
    private static JedisPool jedisPool;// 非切片连接池
    private static ShardedJedisPool shardedJedisPool;// 切片连接池

    static {
        initialPool();
        // initialShardedPool();
    }

    /**
     * 初始化非切片池
     */
    private static void initialPool() {
        if (jedisPool == null) {
            // 池基本配置
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(50);
            config.setMaxIdle(10);
            config.setMaxWaitMillis(10000);
            config.setTestOnBorrow(false);
            jedisPool = new JedisPool(config, SystemConfig.REDIS_IP, SystemConfig.REDIS_PORT);
        }
    }

    /**
     * 初始化切片池
     */
    private static void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        // config.setMaxActive(20);
        config.setMaxIdle(5);
        // config.setMaxWait(1000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(SystemConfig.REDIS_IP, SystemConfig.REDIS_PORT, "master"));
        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    private void show() {
        // ShardedJedis shardedJedis = shardedJedisPool.getResource();
        // jedis = jedisPool.getResource();
        // jedisPool.returnResource(jedis);
        // shardedJedisPool.returnResource(shardedJedis);
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 删除缓存中得对象，根据key
     *
     * @param key
     */
    public static void delete(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("Redis删除出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 删除缓存中得对象，根据keys
     *
     * @param keys
     */
    public static void delete(String... keys) {
        Jedis jedis = getJedis();
        try {
            jedis.del(keys);
        } catch (Exception e) {
            logger.error("Redis批量删除出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 向缓存中设置字符串内容
     *
     * @param key   key
     * @param value value
     */
    public static void saveString(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Redis保存(save)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据key 获取内容
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("Redis获取(get)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * 向缓存中设置字符串内容
     *
     * @param key   key
     * @param value value
     */
    public static void save(String key, Object value) {
        saveByKryo(key, value);
    }

    /**
     * 根据key 获取内容
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return getByKryo(key);
    }

    public static void saveByKryo(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            Kryo kryo = new Kryo();
            // kryo.setReferences(false);
            // kryo.setRegistrationRequired(false);
            // kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            Output output = new Output(BUFFER_SIZE, Integer.MAX_VALUE);
            kryo.writeClassAndObject(output, value);
            output.flush();
            jedis.set(key.getBytes(), output.toBytes());
        } catch (Exception e) {
            logger.error("Redis保存(saveByKryo)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static Object getByKryo(String key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = getJedis();
        try {
            Kryo kryo = new Kryo();
            // Registration registration = kryo.register(clazz);
            // kryo.setReferences(false);
            // kryo.setRegistrationRequired(false);
            // kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            Input input = new Input(jedis.get(key.getBytes()));
            Object result = kryo.readClassAndObject(input);
            input.close();
            return result;
        } catch (Exception e) {
            logger.error("Redis获取(getByKryo)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static void saveByJavaSerializable(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            logger.error("Redis保存(saveByJavaSerializable)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static Object getByJavaSerializable(String key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = getJedis();
        try {

            return SerializeUtil.unserialize(jedis.get(key.getBytes()));
        } catch (Exception e) {
            logger.error("Redis获取(getByKryo)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static void saveByFastJson(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.set(key, JSON.toJSONString(value));
        } catch (Exception e) {
            logger.error("Redis保存(saveByKryo)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据key 获取对象
     *
     * @param key
     * @return
     */
    public static <T> T getByFastJson(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        try {
            String value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            logger.error("Redis获取(getByFastJson)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * 向缓存中设置字符串内容
     *
     * @param key   key
     * @param value value
     */
    public static void saveStringToSortSet(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.zadd(key, jedis.zcount(key, 0, Long.MAX_VALUE) + 1, value);
        } catch (Exception e) {
            logger.error("Redis保存(saveStringToSortSet)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static void deleteStringFromSortSet(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("Redis删除(deleteStringFromSortSet)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static Set<String> listStringFromSortSet(String key, long start, long end) {
        if (key == null) {
            return null;
        }
        Jedis jedis = getJedis();
        try {
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("Redis查询(listStringFromSortSet)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static Long countFromSortSet(String key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = getJedis();
        try {
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("Redis统计条数(countFromSortSet)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * 向缓存中设置字符串内容
     *
     * @param key   key
     * @param value value
     */
    public static void saveStringToList(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("Redis保存(saveStringToList)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static void deleteStringFromList(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.lrem(key, 1, value);
        } catch (Exception e) {
            logger.error("Redis删除(deleteStringFromList)出错：", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static List<String> listStringFromList(String key, long start, long end) {
        if (key == null) {
            return null;
        }
        Jedis jedis = getJedis();
        try {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("Redis查询(listStringFromList)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static Long countFromList(String key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = getJedis();
        try {
            return jedis.llen(key);
        } catch (Exception e) {
            logger.error("Redis统计条数(countFromList)出错：", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }
}