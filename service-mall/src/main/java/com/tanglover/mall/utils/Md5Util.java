package com.tanglover.mall.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author TangXu
 * @create 2019-11-21 14:55
 * @description:
 */
public class Md5Util {

    static Logger logger = LoggerFactory.getLogger(Md5Util.class);

    public final static String md5(String wait4Sign) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        byte[] byteInput = wait4Sign.getBytes();
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(byteInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Md5加签失败", e.getMessage());
        }
        return null;
    }
}