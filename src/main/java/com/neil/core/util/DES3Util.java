/**
 *
 */
package com.neil.core.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author neil 2015-7-1
 * 使用该类方法进行加密必须使用该类方法解密，主要用来加密报文
 */
public class DES3Util {

    /**
     * SecretUtils {3DES加密解密的工具类 }
     *
     * @author William
     * @date 2013-04-19
     */

    // 定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede";
    private static final String PASSWORD_CRYPT_KEY = "9F260862E5FF0367A685EB9F";

    /**
     * 加密方法
     *
     * @param src
     *            源数据的字节数组
     * @return
     */
    public static String encrypt(String source) throws Exception{
        SecretKey deskey = new SecretKeySpec(
                build3DesKey(PASSWORD_CRYPT_KEY), Algorithm); // 生成密钥
        Cipher c1 = Cipher.getInstance(Algorithm); // 实例化负责加密/解密的Cipher工具类
        c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
        return HexUtil.byteToHexString(c1.doFinal(source.getBytes()));
    }

    /**
     * 解密函数
     *
     * @param src
     *            密文的字节数组
     * @return
     */
    public static String decrypt(String source) throws Exception {
        SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY),
                Algorithm);
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
        return new String(c1.doFinal(HexUtil.hexStringToByte(source)));
    }

    /*
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    private static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

		/*
		 * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    public static void main(String[] args) {
        try {
            String str = DES3Util.encrypt("123456saldkfj我是谁");
            System.out.println(str);
            String str2 = DES3Util.decrypt(str);
            System.out.println(str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
