package com.tanglover.zuul.config.filter;

import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;

/**
 * @author TangXu
 * @create 2019-07-22 15:17
 * @description:
 */
public class FileFilterUtil {

    /**
     * @Author TangXu
     * @Description
     * @Date 2019/7/22 15:19
     * @Param [request]
     */
    public static boolean doFileFilter(ServletRequest request) {
        /**
         * 对文件上传处理 不进行解密 直接通过网关
         */
        String contentType = request.getContentType();
        if (contentType != null) {
            MediaType mediaType = MediaType.valueOf(contentType);
            if (MediaType.IMAGE_GIF.includes(mediaType)
                    || MediaType.IMAGE_JPEG.includes(mediaType)
                    || MediaType.IMAGE_PNG.includes(mediaType)
                    || MediaType.APPLICATION_OCTET_STREAM.includes(mediaType)
                    || MediaType.MULTIPART_FORM_DATA.includes(mediaType)
            ) {
                return true;
            }
        }

        return false;
    }
}