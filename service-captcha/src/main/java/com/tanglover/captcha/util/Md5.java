package com.tanglover.captcha.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author TangXu
 * @create 2019-11-21 14:55
 * @description:
 */
public class Md5 {


    public final static String encrypt(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] bytes = s.getBytes();
        //获取MD5摘要算法的MessageDigest对象
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(bytes);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            // char占两个字节
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                //右移四位，高四位清空 取低四位的值
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}