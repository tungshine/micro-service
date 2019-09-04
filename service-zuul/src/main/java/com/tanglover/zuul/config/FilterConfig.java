package com.tanglover.zuul.config;

import com.tanglover.zuul.config.filter.RequestBodyFilter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.RequestContextFilter;

/**
 * @author TangXu
 * @create 2019-04-22 14:27
 * @description:
 */
@Configurable
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new RequestBodyFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}