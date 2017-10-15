package com.neil.core.db.datasource;

public class JdbcContext {
    private static final ThreadLocal<String> context = new ThreadLocal<String>();
    public static void setJdbcType(String jdbcType) {
        context.set(jdbcType);
    }
    public static void setSlave(){
        setJdbcType("read");
    }
    public static void setMaster(){
        clearJdbcType();
    }
    public static String getJdbcType(){
        return (String) context.get();
    }
    public static void clearJdbcType(){
        context.remove();
    }
}
