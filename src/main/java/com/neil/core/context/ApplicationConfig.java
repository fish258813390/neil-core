package com.neil.core.context;

import java.io.File;
import java.util.Properties;

public class ApplicationConfig {
    private final static String CONFIG_ROOT_PATH = System.getProperty("user.dir");

    public static boolean IS_LOCAL = false;

    private static Properties properties = new Properties();

    public static void setAppProperties(Properties propertie){
        properties = propertie;
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
    /**
     * 获取配置文件绝对路径
     * @param fileName
     * @return
     */
    public static String configPath(String fileName){
        if(IS_LOCAL){
            return CONFIG_ROOT_PATH+File.separator+"src"+File.separator+"main"+File.separator
                    +"resources"+File.separator+fileName;
        }else{
            return CONFIG_ROOT_PATH+File.separator+"conf"+File.separator+fileName;
        }
    }

    public static String tmpFileDir(){
        if(IS_LOCAL){
            return CONFIG_ROOT_PATH+File.separator+"src"+File.separator+"main"+File.separator
                    +"resources"+File.separator+"appfile";
        }else{
            return CONFIG_ROOT_PATH+File.separator+"appfile"+File.separator;
        }
    }
}
