package com.neil.core.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class MD5Util {

    private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5Util() {
    }

    /**
     * MD5加密
     * @param b
     * @return 返回16进制字符串
     * @throws Exception
     */
    public static String md5(byte b[]) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    /**
     * MD5加密
     * @param data
     * @return 返回16进制字符串
     * @throws Exception
     */
    public static String md5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte b[] = data.getBytes("UTF8");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            sb.append(byteToHexString(b[i]));

        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5加密
     * @param data
     * @return 返回BASE64字符串
     * @throws Exception
     */
    public static String MD5_BASE64Encoder(String data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        BASE64Encoder encoder = new BASE64Encoder();
        String msg = encoder.encode(md.digest(data.getBytes("ISO-8859-1")));
        return msg;
    }
}
