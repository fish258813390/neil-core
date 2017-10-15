package com.neil.core.context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class YollyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
    private Properties properties = new Properties();
    public void setYollyProp(Set<String> props) {
        for (String propName : props) {
            Properties prop = new Properties();
            try {
                logger.info("Loading properites file from " + ApplicationConfig.configPath(propName));
                prop.load(new FileInputStream(ApplicationConfig.configPath(propName)));
                logger.debug("Properties -> " + prop);
                if(prop != null) {
                    properties.putAll(prop);
                }
            } catch (FileNotFoundException e) {
                logger.error("未找到配置文件"+propName,e);
            } catch (IOException e) {
                logger.error("读取配置文件异常"+propName,e);
            }
        }
        this.setProperties(properties); //关键方法,调用的PropertyPlaceholderConfigurer中的方法,
        //通过这个方法将自定义加载的properties文件加入spring中
        ApplicationConfig.setAppProperties(properties);
    }
}
