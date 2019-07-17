package com.tanglover.mall.api;


import com.alibaba.fastjson.JSONObject;
import com.tanglover.mall.utils.HttpUtils;

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

}
