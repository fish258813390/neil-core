package com.neil.core.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neil.core.config.SystemConfig;

/**
 *
 *
 * <p>
 * Memcache 工具类
 * </p>
 * <p>
 * 	目前该类中主要提供针对命名空间值对象操作方法
 * </p>
 * <p>
 * 	如果无需使用命名空间，请使用getClient() 方法获取net.rubyeye.xmemcached.MemcachedClient 将可使用原生xmemcahced的api方法
 * <br>
 * <br>
 * 例如：MemcachedClient.getClient().add("key", 0, "value");
 * </p>
 *
 * @author neil 2015-6-30
 */
public class MemcachedClient {
    private static final Logger log = LoggerFactory.getLogger(MemcachedClient.class);
    private static MemcachedClientBuilder builder;
    private static net.rubyeye.xmemcached.MemcachedClient  memcachedClient;
    static {
        init();
    }

    private static void init(){
        if(memcachedClient != null && !memcachedClient.isShutdown()){
            return;
        }
        try {
            builder = null;
            memcachedClient = null;
            if(SystemConfig.MEMCACHE_SERVER == null || "".equals(SystemConfig.MEMCACHE_SERVER.trim())){
                log.warn("ZOOKEEPER 配置中未配置MEMCACHE CLIENT初始化地址");
                return;
            }
            builder = new XMemcachedClientBuilder(
                    AddrUtil.getAddresses(SystemConfig.MEMCACHE_SERVER));
            memcachedClient = builder.build();
            log.info("MEMCACHE CLIENT 初始化成功,连接地址:"+SystemConfig.MEMCACHE_SERVER);
        } catch (Exception e){
            log.error("MEMCACHE CLIENT 初始化异常",e);
        }
    }

    public static net.rubyeye.xmemcached.MemcachedClient getClient(){
        return memcachedClient;
    }
    /**
     * 设置数据对象到MEMCACHE
     * @param namespace 命名空间 （建议使用实体名称,以保证全局不重复）
     * @param key 对象的key,key必须唯一
     * @param second 在缓存中保存时间，单位秒，传0则表示永久保存
     * @param object 需要存入缓存的对象（如果是实体对象必须是可序列化的）
     * @return
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws MemcachedException
     */
    public static boolean setWithNamespace(String namespace,final String key,final int second,final Object object) throws MemcachedException, InterruptedException, TimeoutException{
        return memcachedClient.withNamespace(namespace,
                new MemcachedClientCallable<Boolean>() {
                    public Boolean call(net.rubyeye.xmemcached.MemcachedClient client)
                            throws MemcachedException, InterruptedException,
                            TimeoutException
                    {
                        return client.set(key,second ,object);
                    }
                });
    }
    /**
     * 根据key获取对象值
     * @param namespace 命名空间 （建议使用实体名称,以保证全局不重复）
     * @param key 对象的key,key必须唯一
     * @return
     */
    public static Object getWithNamespace(String namespace,final String key) throws MemcachedException, InterruptedException, TimeoutException{
        return memcachedClient.withNamespace(namespace,
                new MemcachedClientCallable<Object>() {
                    public Object call(net.rubyeye.xmemcached.MemcachedClient client)
                            throws MemcachedException, InterruptedException,
                            TimeoutException {
                        return client.get(key);
                    }
                });
    }

    /**
     * 通过命名空间和key集合获取MAP对象
     * @param namespace
     * @param keyList
     * @return
     * @throws MemcachedException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static Map<String, Object> getWithNamespaceByListKey(String namespace,final List<String> keyList) throws MemcachedException, InterruptedException, TimeoutException{
        return memcachedClient.withNamespace(namespace,
                new MemcachedClientCallable<Map<String, Object>>() {
                    public Map<String, Object> call(net.rubyeye.xmemcached.MemcachedClient client)
                            throws MemcachedException, InterruptedException,
                            TimeoutException {
                        return client.get(keyList);
                    }
                });
    }

    /**
     * 根据命名空间和key删除对象值
     * @param namespace 命名空间 （建议使用实体名称,以保证全局不重复）
     * @param key 对象的key,key必须唯一
     * @return
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws MemcachedException
     */
    public static boolean deleteWithNamespace(String namespace,final String key) throws MemcachedException, InterruptedException, TimeoutException{

        return memcachedClient.withNamespace(namespace,
                new MemcachedClientCallable<Boolean>() {
                    public Boolean call(net.rubyeye.xmemcached.MemcachedClient client)
                            throws MemcachedException, InterruptedException,
                            TimeoutException
                    {
                        return client.delete(key);
                    }
                });
    }
    /**
     * 使某个命名空间失效
     * @param namespace
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws MemcachedException
     */
    public static void invalidateNamespace(String namespace) throws MemcachedException, InterruptedException, TimeoutException{
        memcachedClient.invalidateNamespace(namespace);
    }
}
