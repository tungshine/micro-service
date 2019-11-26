package com.tanglover.mall.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.tanglover.mall.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author TangXu
 * @create 2019-11-22 13:57
 * @description:
 */
//@Component
public class TimestampInterceptor implements HandlerInterceptor {

    static Logger logger = LoggerFactory.getLogger(TimestampInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (method.equals(RequestMethod.OPTIONS)) {
            return true;
        }
//        PrintWriter out = response.getWriter();
        JSONObject json = HttpUtils.getJSONObject(request);
        if (null == json) {
            return false;
        }
        long timestamp = json.getLongValue("timestamp");
        if (0L == timestamp) {
//            out.write("timestamp不能为空");
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long expire = TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES);
        expire = expire + timestamp;
        if (timestamp >= currentTimeMillis || currentTimeMillis < expire) {
//            out.write("请求时间超限");
            logger.warn("请求时间超限");
            return false;
        }
//        out.flush();
        return true;
    }
}