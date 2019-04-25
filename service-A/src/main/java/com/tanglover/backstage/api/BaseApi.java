package com.tanglover.backstage.api;


import com.alibaba.fastjson.JSONObject;
import com.tanglover.backstage.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseApi {

    public byte[] fallbackError(HttpServletRequest request, HttpServletResponse response) {
        return HttpUtils.errorBytes(request, response, 500, "The state of the network is not good");
    }

    public Map<String, Object> fallbackError(Map<String, Object> data) {
        return returnError(500, "The state of the network is not good");
    }

    public Map<String, Object> fallbackError(Map<String, Object> data, HttpServletRequest request) {
        return returnError(500, "The state of the network is not good");
    }

    /**
     * 成功的时候反馈
     *
     * @param return_jsonData
     * @return
     */
    //by jxh 2018-9-12
    public Map<String, Object> returnSuccess(JSONObject return_jsonData) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        map.put("result", 0);
        map.put("data", return_jsonData);
        return map;
    }

    // by TangXu 2018-10-24
    public Map<String, Object> returnSuccess(Map<String, Object> returnMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        map.put("result", 0);
        map.put("data", returnMap);
        return map;
    }

    // by chenchun 2018-11-26
    public Map<String, Object> returnSuccess(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        map.put("result", 0);
        map.put("data", object);
        return map;
    }

    /**
     * 错误的时候反馈
     *
     * @param error_code
     * @param msg
     * @return
     */
    //by jxh 2018-9-12
    public Map<String, Object> returnError(int error_code, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", msg);
        map.put("result", error_code);
        return map;
    }

    /**
     * 获取IP
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception ignored) {
        }
        return ip == null ? "" : ip;
    }

}
