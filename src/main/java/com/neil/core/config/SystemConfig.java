package com.neil.core.config;


import com.neil.core.context.ApplicationConfig;

public class SystemConfig {

    // @ZooKeeper("/yolly/properties/redis.ip")
    public static String REDIS_IP = ApplicationConfig.getProperty("redis.ip");
    // @ZooKeeper("/yolly/properties/redis.port")
    public static int REDIS_PORT = Integer.valueOf(ApplicationConfig.getProperty("redis.port"));

    public static String MEMCACHE_SERVER = ApplicationConfig.getProperty("memcache.server");

    public static String SERVICE_CODE = ApplicationConfig.getProperty("service.code");

}
