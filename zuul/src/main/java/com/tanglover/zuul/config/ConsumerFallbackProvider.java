package com.tanglover.zuul.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-05-14 10:41
 * @description:
 */
@Component
public class ConsumerFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
        return "service-A";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                // fallback时的状态码
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                // 数字类型的状态码，本例返回的其实就是200，详见HttpStatus
                return this.getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                // 状态文本，本例返回的其实就是OK，详见HttpStatus
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                // 响应体
                String responseText = route + "正在更新中，请稍后再试。";
                JSONObject result = new JSONObject();
                result.put("code", 10000);
                result.put("msg", responseText);
                return new ByteArrayInputStream(result.toJSONString().getBytes("UTF-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers configuration
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

}