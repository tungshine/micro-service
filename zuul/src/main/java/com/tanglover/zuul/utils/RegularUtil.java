package com.tanglover.zuul.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @Author: TangXu
 * @Date: 2018/7/5 16:04
 * @Description:
 */
public class RegularUtil {
    public static Logger logger = LoggerFactory.getLogger(RegularUtil.class);

    /**
     * 邮件注册正则表达式--判断是否是恶意正则
     */
    public static boolean isSpiteRegular(String content, String pattern) {


        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(pattern)) {
            return false;
        }

        //判断正则表达式是否是多个,逗号分隔
        String[] config_array = pattern.split(",");

        //检查格式如果 还是没有可供筛选的数据 也允许直接通过
        if (config_array == null || config_array.length < 1) {
            return false;
        }

        //遍历
        for (String pattern_item : config_array) {
            boolean flag = Pattern.matches(pattern_item, content);
            if (flag) {
                //匹配上
                return true;
            }
        }


        return false;
    }

    /**
     * 判断是否是允许的邮箱后缀 例如: 163.com,126.com 等
     *
     * @return true:允许 false:不允许
     */
    public static boolean isAllowEmilSuffixes(String allow_email_suffixes, String email) {

//        logger.info("-------allow_email_suffixes--------{}",allow_email_suffixes);

        if (StringUtils.isEmpty(allow_email_suffixes) || StringUtils.isEmpty(email)) {
            //允许--全部通过
            return true;
        }

        //判断正则表达式是否是多个,逗号分隔
        String[] config_array = allow_email_suffixes.split(",");

        //检查格式如果 还是没有可供筛选的数据 也允许直接通过
        if (config_array == null || config_array.length < 1) {
            //允许--全部通过
            return true;
        }

        //截取邮箱后缀@
        int index = email.indexOf("@");
        if (index == -1) {
            //不允许--不是邮箱格式
            return false;
        }

        String suffixes = email.substring(index + 1);
        if (StringUtils.isEmpty(suffixes)) {
            //不允许--不是邮箱格式
            return false;
        }

        //遍历
        for (String pattern_item : config_array) {
            boolean flag = pattern_item.equals(suffixes);
            if (flag) {
                //匹配上
                return true;
            }
        }

        return false;

    }



    /*public static void main(String[] args) {
        String content="nr12345679mn@163.com";
        String pattern = "[a-zA-Z]{2}[0-9]{8}[a-zA-Z]{2}@163.com";
        System.out.println(isSpiteRegular( content, pattern));
    }*/
}
