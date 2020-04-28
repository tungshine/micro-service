package com.tanglover.mall.api;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TangXu
 * @create 2020-04-27 16:16
 * @description:
 */
@RestController
public class FileApi extends BaseApi {


    @PostMapping("/upload")
    public String upload(HttpServletRequest request) {
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart(request);
//        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartHttpServletRequest.getMultiFileMap();
        String fileName = multiFileMap.get("file").get(0).getOriginalFilename();
        return fileName;
    }
}