package com.tanglover.captcha.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author TangXu
 * @create 2019-11-21 14:35
 * @description:
 */
public class Md5Sign {


    public static boolean verifyRequest(JSONObject json, int section, TimeUnit timeUnit) {
        String sign = json.getString("sign");
        if (null == sign || "" == sign) {
            return false;
        }
        String requestParam = buildRequestParam(json);
        String md5Sign = Md5.encrypt(requestParam);
        if (md5Sign.toUpperCase().equalsIgnoreCase(sign)) {
            return true;
        }
        return false;
    }

    public static String buildRequestParam(JSONObject json) {
        Map<String, Object> map = JSON.parseObject(json.toJSONString(), Map.class);
        Map<String, Object> sortMap = sortByKey(map);
        String waitForSign = "";
        for (String key : sortMap.keySet()) {
            if (!key.equalsIgnoreCase("sign")) {
                waitForSign += key + "=" + sortMap.get(key) + "&";
            }
        }
        waitForSign = waitForSign.substring(0, waitForSign.length() - 1);
        return waitForSign;
    }

    public static Map<String, Object> sortByKey(Map<String, Object> map) {
        if (null == map || map.isEmpty()) {
            return null;
        }
//        Map<String, Object> sortMap = new TreeMap<>(String::compareTo);
        Map<String, Object> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        sortMap.putAll(map);
        return sortMap;
    }
}