package com.tanglover.mall.utils;

/**
 * @author TangXu
 * @create 2020-04-18 23:07
 * @description:String工具类
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String camel2Underline(String camelStr) {
        return convertCamel(camelStr, '_');
    }

    public static String convertCamel(String camelStr, char separator) {
        if (isEmpty(camelStr)) {
            return camelStr;
        }
        StringBuilder out = new StringBuilder();
        char[] strChar = camelStr.toCharArray();
        for (int i = 0; i < strChar.length; i++) {
            char c = strChar[i];
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    out.append(separator);
                }
                out.append(Character.toLowerCase(c));
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    public static void main(String[] args) {
        System.out.println(camel2Underline("UserInfo"));
    }

}