package com.tanglover.zuul.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.tanglover.zuul.config.Configuration;
import com.tanglover.zuul.error.ErrorCodeConstant;
import com.tanglover.zuul.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.RequestContentDataExtractor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author TangXu
 * @create 2019-07-22 17:44
 * @description: 代理进行适配, 避免对数据重复encode
 */
@Component
public class FormBodyWrapperFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(FormBodyWrapperFilter.class);

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    @Autowired
    private Configuration config;
    @Autowired
    private IpUtils ipUtils;

    private FormHttpMessageConverter formHttpMessageConverter;
    private Field requestField;
    private Field servletRequestField;

    public FormBodyWrapperFilter() {
        this(new AllEncompassingFormHttpMessageConverter());
    }

    public FormBodyWrapperFilter(FormHttpMessageConverter formHttpMessageConverter) {
        this.formHttpMessageConverter = formHttpMessageConverter;
        this.requestField = ReflectionUtils.findField(HttpServletRequestWrapper.class, "req", HttpServletRequest.class);
        this.servletRequestField = ReflectionUtils.findField(ServletRequestWrapper.class, "request",
                ServletRequest.class);
        Assert.notNull(this.requestField, "HttpServletRequestWrapper.req field not found");
        Assert.notNull(this.servletRequestField, "ServletRequestWrapper.request field not found");
        this.requestField.setAccessible(true);
        this.servletRequestField.setAccessible(true);
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String contentType = request.getContentType();


        // Don't use this filter on GET method
        if (contentType == null) {
            return true;
        }
        // Only use this filter for form data and only for multipart data in a
        // DispatcherServlet handler
        try {
            MediaType mediaType = MediaType.valueOf(contentType);
            return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType)
                    || MediaType.APPLICATION_OCTET_STREAM.includes(mediaType)
                    || (isDispatcherServletRequest(request) && MediaType.MULTIPART_FORM_DATA.includes(mediaType)
                    || MediaType.APPLICATION_JSON.includes(mediaType)
                    || MediaType.IMAGE_GIF.includes(mediaType)
                    || MediaType.IMAGE_JPEG.includes(mediaType)
                    || MediaType.IMAGE_PNG.includes(mediaType)
                    || MediaType.APPLICATION_PDF.includes(mediaType)
            );
        } catch (InvalidMediaTypeException ex) {
            return false;
        }
    }

    private boolean isDispatcherServletRequest(HttpServletRequest request) {
        return request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null;
    }

    /**
     * 对网络请求进行拦截处理
     *
     * @return
     */
    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String remoteAddr = request.getRemoteAddr();

        // Pass remote address downstream by setting X-Forwarded for header again on Zuul request
        logger.info("Settings X-Forwarded-For to: {}", remoteAddr);
//		ctx.getZuulRequestHeaders().put(X_FORWARDED_FOR, remoteAddr);
        ctx.getZuulRequestHeaders().put(X_FORWARDED_FOR, IpUtils.getIpAddr(request));
        try {
            ctx.getZuulRequestHeaders().put(X_REAL_IP, request.getHeader("X-Real-IP"));
        } catch (Exception e) {

        }


        /**
         * 如果是文件 则不进行过滤内容解析
         */
        if (FileFilterUtil.doFileFilter(request)) {
            return null;
        }


        //拦截检索
        if (preventIp(request)) {
            try {
                // 1.对内容格式进行验证
                JSONObject json = null;
                byte[] body = HttpUtils.readBody(request);
                if (body.length > 0) {
                    json = HttpUtils.decrypt(body);
                }

                if (json == null) {
                    logger.debug("body is null");
                    try {
                        json = JSONObject.parseObject(new String(body, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.error("请求数据转换json错误.....", e);
                        json = new JSONObject();
                    }
                }

                //by jxh 2018-5-31 对IP拦截处理
                if (json != null && !json.isEmpty() && json.containsKey("pwd")) {
                    json.put("pwd", "");
                }

                logger.info("ppgame_marker:forbidden:请求接口={},IP={}，请求数据={}", request.getRequestURI(), IpUtils.getIpAddr(request), json.toJSONString());

                //设置返回数据
                body = HttpUtils.errorBytes(ctx.getResponse(), ErrorCodeConstant.ERROR_INTERCEPTOR.code, ErrorCodeConstant.ERROR_INTERCEPTOR.message);
                ctx.setResponseBody(new String(body, "UTF-8"));

            } catch (Exception e) {
                logger.error("请求错误", e);
            }
            return null;
        }


        //进行请求数据包装转发
        FormBodyRequestWrapper wrapper = null;
        if (request instanceof HttpServletRequestWrapper) {
            HttpServletRequest wrapped = (HttpServletRequest) ReflectionUtils.getField(this.requestField, request);
            wrapper = new FormBodyRequestWrapper(wrapped);
            ReflectionUtils.setField(this.requestField, request, wrapper);
            if (request instanceof ServletRequestWrapper) {
                ReflectionUtils.setField(this.servletRequestField, request, wrapper);
            }
        } else {
            wrapper = new FormBodyRequestWrapper(request);
            ctx.setRequest(wrapper);
        }
        if (wrapper != null) {
            ctx.getZuulRequestHeaders().put("content-type", wrapper.getContentType());
            int length = wrapper.getContentLength();
            if (length == 0) {
                return null;
            }
            logger.info("--------------length={}---------------", length);

            //这里检查是否存在异常 如果存在 则做异常处理 就不进行后面的过滤器处理了
            if (wrapper.getExistException()) {
                //存在异常
                //过滤该请求，不往下级服务去转发请求，到此结束
                // 过滤该请求，不对其进行路由
                ctx.setSendZuulResponse(false);
                // 返回错误码
                ctx.setResponseStatusCode(401);
                ctx.set("isSuccess", false);
                //设置返回数据
                try {
                    byte[] body = HttpUtils.errorBytes(ctx.getResponse(), ErrorCodeConstant.ERROR_INTERCEPTOR.code, ErrorCodeConstant.ERROR_INTERCEPTOR.message);
                    ctx.setResponseBody(new String(body, "UTF-8"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 包装HttpServletRequest 进行数据重组
     */
    private class FormBodyRequestWrapper extends HttpServletRequestWrapper {

        private HttpServletRequest request;
        private byte[] contentData;
        private MediaType contentType;
        private int contentLength;

        /**
         * 是否存在异常 true : 存在 false:不存在
         */
        private boolean ExistException = false;

        public FormBodyRequestWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        public boolean getExistException() {
            return ExistException;
        }

        public void setExistException(boolean existException) {
            ExistException = existException;
        }

        @Override
        public String getContentType() {
            if (this.contentData == null) {
                buildContentData();
            }
            if (this.contentType == null) {
                return null;
            }
            return this.contentType.toString();
        }

        @Override
        public int getContentLength() {
            if (super.getContentLength() <= 0) {
                return super.getContentLength();
            }
            if (this.contentData == null) {
                buildContentData();
            }
            return this.contentLength;
        }

        @Override
        public long getContentLengthLong() {
            return getContentLength();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (this.contentData == null) {
                buildContentData();
            }
            return new ServletInputStreamWrapper(this.contentData);
        }

        /**
         * 对请求流里面的数据进行解析重组
         */
        private synchronized void buildContentData() {
            try {
                MultiValueMap<String, Object> builder = RequestContentDataExtractor.extract(this.request);
                //设置输出流--response数据的内容格式等
                FormHttpOutputMessage data = new FormHttpOutputMessage();

                if (this.request.getContentType() == null) {
                    this.contentType = MediaType.parseMediaType("application/x-www-form-urlencoded");
                } else {
                    this.contentType = MediaType.valueOf(this.request.getContentType());
                }
                data.getHeaders().setContentType(this.contentType);

                // 1.对内容格式进行验证
                JSONObject json = null;
                byte[] body = null;
                try {
                    // 从body读取数据
                    body = HttpUtils.readBody(request);
                    if (body.length > 0) {
                        //这里判断是JS请求过来的 还是  APP请求过来的
                        String pjd_encrypt = request.getHeader("pjd_encrypt");
                        String pjdencrypt = request.getHeader("pjdencrypt");
                        if ((null != pjd_encrypt && "9527".equalsIgnoreCase(pjd_encrypt))
                                || (null != pjdencrypt && "9527".equalsIgnoreCase(pjdencrypt))) {
                            logger.info("pjd_encrypt===================================" + pjd_encrypt);
                            json = HttpUtils.decryptJsBase64(body);
                            if (json == null || json.isEmpty()) {
                                logger.info("pjd_encrypt2222222==========333=========================222222");
                            }
                        } else {
                            json = HttpUtils.decrypt2(body);
                        }
                    }
                } catch (Exception e) {
                    logger.error("decrypt failed", e);
                }

                if (json != null) {
                    this.contentData = json.toString().getBytes();
                } else {
                    logger.debug("body is null  没有进行加密传递");

                    this.contentData = "{\"message\":\"data is not entry\",\"error\":1}".getBytes();
                }
                this.contentLength = this.contentData.length;

                //by jxh 2018-5-31 对IP拦截处理
                if (json != null && !json.isEmpty() && json.containsKey("pwd")) {
                    json.put("pwd", "");
                    //设置请求IP 微服务好获取
                    String ip = IpUtils.getIpAddr(request);
                    if (StringUtils.hasLength(ip)) {
                        json.put("ip", ip);
                    }
                }

                //by jxh 2018-7-5 对邮箱进行正则表达式检查
                if (ipUtils.isExistForbiddenEmail(config, request, json)) {
                    //throw new IllegalStateException("There is exist Spited  the  email of registered");
                    logger.info("-----------------setExistException(true)------------------------------");
                    setExistException(true);
                }

            } catch (Exception e) {
                throw new IllegalStateException("Cannot convert form data", e);
            }
        }

        private byte[] getData(MultiValueMap<String, Object> builder) {
            for (Map.Entry<String, List<Object>> entry : builder.entrySet()) {
                String name = entry.getKey();
                return name.getBytes();
            }
            return null;
        }

        private class FormHttpOutputMessage implements HttpOutputMessage {
            private HttpHeaders headers = new HttpHeaders();
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public HttpHeaders getHeaders() {
                return this.headers;
            }

            @Override
            public OutputStream getBody() throws IOException {
                return this.output;
            }

            public byte[] getInput() throws IOException {
                this.output.flush();
                return this.output.toByteArray();
            }
        }
    }

    /***
     * 处理防刷IP拦截
     * @return true:该IP需要被拦截
     */
    private boolean preventIp(HttpServletRequest request) {

        try {
            //首先判断 禁用的IP段功能开关 0:开启 1：关闭  是否开启
            int state = 0;
            if (StringUtils.hasLength(config.getFORBIDDEN_IPS_STATE())) {
                state = Integer.parseInt(config.getFORBIDDEN_IPS_STATE().trim());
            }
            if (state == 1) {
                return false;
            }
            String this_request_uri = request.getRequestURI().replace("/", "");
            //对需要筛选的接口进行判定
            String request_uri = config.getFORBIDDEN_IPS_URI();
            if (!StringUtils.hasLength(request_uri)) {
                return false;
            }
            if (request_uri.indexOf(this_request_uri) == -1) {
                //该次请求的接口 不在我们限制的接口范围内 直接返回正常流程
                return false;
            }
            //下面进行IP判定处理
            String ip = IpUtils.getIpAddr(request);
            if (!StringUtils.hasLength(ip)) {
                return false;
            }
            //对IP获取代理入口IP--即 客户端真实IP
            ip = ip.split(",")[0];
            //IP白名单检查处理
            if (ipUtils.isAllowIp(ip, config.getALLOW_IPS())) {
                //正常返回--不予拦截
                return false;
            }
            //IP黑名单检查处理
            if (ipUtils.isForbiddenIp(ip, config.getFORBIDDEN_IPS())) {
                //拦截处理
                return true;
            }
            //程序自动算法进行IP处理
            long expireSeconds = Integer.parseInt(config.getFORBIDDEN_IPS_TIME());
            int allow_count = Integer.parseInt(config.getFORBIDDEN_IPS_COUNT());
            if (ipUtils.RecordIPAndCount(ip, expireSeconds, allow_count)) {
                //超出了允许次数 把IP加入黑名单 直接拒绝请求
                return true;
            }
        } catch (Exception e) {
            logger.error("防刷功能检查处理失败......", e);
        }
        return false;
    }
}