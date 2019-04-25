package com.tanglover.zuul.config.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author TangXu
 * @create 2019-04-22 14:43
 * @description: body包装器
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private Logger logger = LoggerFactory.getLogger(BodyReaderHttpServletRequestWrapper.class);

    private byte[] body = new byte[0];

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);

        // 开始读取body内容
        int length = request.getContentLength();
        if (1 != length) {
            try {
                body = IOUtils.toByteArray(request.getInputStream());
            } catch (IOException e) {
                body = new byte[0];
            }
            logger.debug("读取body内容长度为：{}", body.length);
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

    }
}