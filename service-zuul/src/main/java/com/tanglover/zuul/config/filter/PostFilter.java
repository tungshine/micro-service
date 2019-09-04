package com.tanglover.zuul.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.tanglover.zuul.elk.RequestMessageModel;
import com.tanglover.zuul.error.ErrorCodeConstant;
import com.tanglover.zuul.utils.HttpUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * @author TangXu
 * @create 2019-07-22 14:41
 * @description:
 */
@Component
public class PostFilter extends ZuulFilter {

    Logger logger = LoggerFactory.getLogger(PostFilter.class);

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        /**
         * 如果是文件 则不进行过滤内容解析
         */
//        if (FileFilterUtil.doFileFilter(request)) {
//            return null;
//        }
        try {
            //这里截取返回数据
            String response_content = "";
            if (ctx.getResponseStatusCode() == HttpStatus.SC_OK) {
                // 因为这里没有使用restFull  所以业务服务器正常返回状态码
                logger.info("request.getMethod()=={}", request.getMethod());
                if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
                    logger.info("路由请求之后的数据拦截: 请求接口={},IP={},requestParam={}", request.getRequestURI(), IpUtils.getIpAddr(request), request.getQueryString());
                    return null;
                }

                InputStream stream_request = request.getInputStream();
                String request_content = StreamUtils.copyToString(stream_request, Charset.forName("UTF-8"));

                InputStream stream_response = ctx.getResponseDataStream();
                response_content = StreamUtils.copyToString(stream_response, Charset.forName("UTF-8"));

                if (request_content != null && response_content != null) {
                    JSONObject json_request = null;
                    JSONObject json_response = null;
                    try {
                        json_request = HttpUtils.convertHttpContent(request_content);
                        json_response = HttpUtils.convertHttpContent(response_content);
                    } catch (Exception e) {
                        logger.error("request_content=========================={},response_content======={}", request_content, response_content);
                    }
                    if (json_request != null && json_response != null) {
                        RequestMessageModel rmm = new RequestMessageModel();
                        rmm.setIP(IpUtils.getIpAddr(request));
                        rmm.setProject("account_read_zuul");
                        rmm.setRequestJSON(json_request);
                        rmm.setResponseJSON(json_response);
                        rmm.setRequestUri(request.getRequestURI());
                        logger.info(MarkerFactory.getMarker("account_read_zuul"), JSONObject.toJSONString(rmm));
                    }
                    //返回结果
                    ctx.setResponseBody(response_content);
                } else {
                    //异常数据 直接返回统一说明
                    //设置返回数据
                    byte[] body = HttpUtils.errorBytes(ctx.getResponse(), ErrorCodeConstant.ERROR_INTERCEPTOR.code, ErrorCodeConstant.ERROR_INTERCEPTOR.message);
                    ctx.setResponseBody(new String(body, "UTF-8"));
                }

            } else {
                //业务服务器返回错误状态码---暂时不给予处理
            }

        } catch (Exception e) {
            logger.error("解析response 返回数据错误", e);

            //设置返回数据
            try {
                byte[] body = HttpUtils.errorBytes(ctx.getResponse(), ErrorCodeConstant.ERROR_INTERCEPTOR.code, ErrorCodeConstant.ERROR_INTERCEPTOR.message);
                ctx.setResponseBody(new String(body, "UTF-8"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}