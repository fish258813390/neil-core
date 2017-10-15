package com.neil.core.zookeeper;

import java.util.Map;

public interface Config {

    public byte[] getConfig(String path) throws Exception;

    public Map<String, String> getConfigs(String path) throws Exception;

}
