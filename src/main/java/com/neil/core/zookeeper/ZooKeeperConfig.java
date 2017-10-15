package com.neil.core.zookeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.neil.core.zookeeper.Config;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperConfig implements Config {

    private Properties properties;

    public ZooKeeperConfig(Properties properties) {
        this.properties = properties;
    }

    @Override
    public byte[] getConfig(String path) throws Exception {
        CuratorFramework client = ZooKeeperFactory.get(properties);
        if (!exists(client, path)) {
            throw new RuntimeException("zookeeper path " + path + " does not exists.");
        }
        return client.getData().forPath(path);
    }

    @Override
    public Map<String, String> getConfigs(String path) throws Exception {
        CuratorFramework client = ZooKeeperFactory.get(properties);
        if (!exists(client, path)) {
            throw new RuntimeException("zookeeper path " + path + " does not exists.");
        }
        List<String> keys = client.getChildren().forPath(path);
        Map<String, String> result = new HashMap<String, String>();
        for (String key : keys) {
            result.put(key, new String(client.getData().forPath(path + "/" + key), "UTF-8"));
        }
        return result;
    }

    private boolean exists(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        return !(stat == null);
    }

}
