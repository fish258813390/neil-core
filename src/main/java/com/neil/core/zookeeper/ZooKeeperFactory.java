package com.neil.core.zookeeper;

import java.util.Properties;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperFactory {

    public static CuratorFramework get(Properties properties) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.valueOf(properties.getProperty("zoo.baseSleepTimems")),
                Integer.valueOf(properties.getProperty("zoo.maxRetries")));
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(properties.getProperty("zoo.server")).retryPolicy(retryPolicy)
                .namespace(properties.getProperty("zoo.properties.namespace")).build();
        client.start();
        return client;
    }
}
