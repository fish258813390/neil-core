package com.neil.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 根据规定格式 和日期或者日期时间字符串
     * @param format
     * @param date
     * @return
     */
    public static String getDateTime(String format,Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 日期字符串转日期
     * @param format
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static Date getDate(String format,String dateStr) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * 根据规定格式获取当前日期时间字符串
     * @param format
     * @return
     */
    public static String getDateTime(String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取系统当前日期时间字符串
     * <br>
     * 格式:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 根据指定的日期 获取日期时间字符串
     * <br>
     * 格式：yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getDateTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}
