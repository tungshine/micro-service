package com.tanglover.security.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-08-15 18:05
 * @description:
 */
public class Result {

    private String msg;
    private int code;
    private Object data;

    public static String returnSuccessString(String msg, Object data) {
        JSONObject result = new JSONObject();
        result.put("msg", msg);
        result.put("code", 0);
        result.put("data", data);
        return result.toJSONString();
    }

    public static Map<String, Object> returnSuccessMap(String msg, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg);
        result.put("code", 0);
        result.put("data", data);
        return result;
    }

    public static String returnFailureString(String msg, int code) {
        JSONObject result = new JSONObject();
        result.put("msg", msg);
        result.put("code", code);
        return result.toJSONString();
    }

    public static Map<String, Object> returnFailureMap(String msg, int code) {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg);
        result.put("code", code);
        return result;
    }
}