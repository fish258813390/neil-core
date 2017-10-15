package com.neil.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
    private static int doubleDigit =0;

    /**
     * 返回两位数的随机号
     * @return
     */
    public static synchronized int getDoubleDigit()
    {
        doubleDigit++;
        if (doubleDigit == 100)
            doubleDigit = 1;
        return doubleDigit;
    }

    public static String numberFormat(int num, int length)
    {
        DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
        StringBuffer format = new StringBuffer("");
        for (int i = 0; i < length; i++){
            format.append("0");
        }
        df.applyPattern(format.toString());
        return df.format(num);
    }

    public static String numberFormat(long num, int length)
    {
        DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
        StringBuffer format = new StringBuffer("");
        for (int i = 0; i < length; i++){
            format.append("0");
        }
        df.applyPattern(format.toString());
        return df.format(num);
    }

    public static BigDecimal stringToDecimal(String num)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        BigDecimal b = new BigDecimal(df.format(Double.parseDouble(num)));
        return b;
    }

}
