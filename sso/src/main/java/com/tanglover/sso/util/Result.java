package com.tanglover.sso.util;

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

    public static String returnSuccessString(String msg, int code, Object data) {
        JSONObject result = new JSONObject();
        result.put("msg", msg);
        result.put("code", code);
        result.put("data", data);
        return result.toJSONString();
    }

    public static Map<String, Object> returnSuccessMap(String msg, int code, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg);
        result.put("code", code);
        result.put("data", data);
        return result;
    }
}