package com.tanglover.zuul.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author TangXu
 * @create 2019-04-22 14:39
 * @description:
 */
public class RequestBodyFilter implements javax.servlet.Filter {

    private Logger logger = LoggerFactory.getLogger(RequestBodyFilter.class);
    static final String UNKNOWN = "unknown";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("加载filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            logger.info("--------ip={}, requestUrl={}", getIpAddress((HttpServletRequest) servletRequest), ((HttpServletRequest) servletRequest).getRequestURI());
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    public String getIpAddress(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("X-Forwarded-For");
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception ignored) {
        }
        return ip == null ? "" : ip;
    }
}