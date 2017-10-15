package com.neil.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 读取properties文件
 * @author houwen
 *
 */
public class PropertiesUtil {

    private static Properties pros = null;

    static{
        pros = new Properties();
    }

    /**
     * 加载文件
     * @param fileName
     */
    public static void load(String fileName){
        InputStream in = PropertiesUtil.class.getResourceAsStream(fileName);
        try {
            pros.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件加载属性值
     * @param fileName
     * @param key
     * @return
     */
    public static String loadValue(String fileName,String key){
        load(fileName);
        return get(key);
    }

    /**
     * 获得配置文件属性值
     * @param key
     * @return
     */
    public static String get(String key){
        try {
            return new String(pros.getProperty(key).getBytes("ISO8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从文件加载一个字符串数组
     * @param fileName
     * @param key
     * @return
     */
    public static String[] loadStrArray(String fileName,String key){
        load(fileName);
        return getStrArray(key);
    }

    /**
     * 将配置文件中","分割的字符串转换成字符串数组
     * @param key
     * @return
     */
    public static String[] getStrArray(String key){
        return get(key).split(",");
    }

    public static String getValue(String file,String key){
        try {
            File f = new File(file);
            Properties p = new Properties();
            p.load(new FileInputStream(f));
            return String.valueOf(p.get(key));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setValue(String path,String key,String value){
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(path));
            pros.setProperty(key, value);
            pros.store(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}


