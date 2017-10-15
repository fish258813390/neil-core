package com.neil.core.zookeeper;

import com.neil.core.context.ApplicationConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ZooKeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    public static final String PATH = "zoo.properties.path";

    private Properties properties = new Properties();

    public void setYollyProp(Set<String> props) {
        for (String propName : props) {
            Properties prop = new Properties();
            try {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propName);
                prop.load(inputStream);
                logger.debug("Properties -> " + prop);
                if (prop != null) {
                    properties.putAll(prop);
                }
            } catch (FileNotFoundException e) {
                logger.error("未找到配置文件" + propName, e);
            } catch (IOException e) {
                logger.error("读取配置文件异常" + propName, e);
            }
        }
        try {
            loadZookeeperProperties();
        } catch (Exception e) {
            logger.error("加载zookeeper配置信息异常", e);
        }
        this.setProperties(properties); // 关键方法,调用的PropertyPlaceholderConfigurer中的方法,
        // 通过这个方法将自定义加载的properties文件加入spring中
        ApplicationConfig.setAppProperties(properties);
    }

    private void loadZookeeperProperties() throws Exception {
        String path = properties.getProperty(PATH);
        Config config = new ZooKeeperConfig(properties);
        Map<String, String> data = config.getConfigs(path);
        Set<String> keys = data.keySet();
        for (String key : keys) {
            properties.put(key, data.get(key));
        }
    }

    private void loadZookeeperPropertie() throws Exception {
        String path = properties.getProperty(PATH);
        Config config = new ZooKeeperConfig(properties);
        byte[] data = config.getConfig(path);
        String cfg = new String(data, "UTF-8");
        if (StringUtils.isNotBlank(cfg)) {
            // 完整的应该还需要处理：多条配置、value中包含=、忽略#号开头
            String[] cfgItem = StringUtils.split(cfg, "=");
            properties.put(cfgItem[0], cfgItem[1]);
        }
    }

}
