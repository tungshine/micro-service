package com.tanglover.mall.api;


import com.tanglover.mall.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author TangXu
 * @Description
 * @Date 2019/9/17 13:57
 */
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

    public Map<String, Object> returnMessage(String msg, int code, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("result", code);
        map.put("data", object);
        return map;
    }

    /**
     * 成功的时候反馈
     *
     * @param object
     * @return
     */
    public Map<String, Object> returnSuccess(Object object) {
        return returnMessage("success", 0, object);
    }

    /**
     * 错误的时候反馈
     *
     * @param error_code
     * @param msg
     * @return
     */
    public Map<String, Object> returnError(int error_code, String msg) {
        return returnMessage(msg, error_code, null);
    }

}
