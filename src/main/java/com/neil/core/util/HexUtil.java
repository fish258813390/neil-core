/**
 *
 */
package com.neil.core.util;

/**
 * @author neil 2014-9-10
 *
 */
public class HexUtil {
    public static final String byteToHexString(byte bArray[])
    {
        StringBuffer sb = new StringBuffer(bArray.length * 2);
        for (int i = 0; i < bArray.length; i++)
        {
            String sTemp = Integer.toHexString(0xff & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hexStringToByte(String hexString)
    {
        if (hexString == null || hexString.equals(""))
            return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char hexChars[] = hexString.toCharArray();
        byte d[] = new byte[length];
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2;
            d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c)
    {
        byte b = (byte)"0123456789ABCDEF".indexOf(c);
        return b;
    }
}
