package com.tanglover.mall.api;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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


    @RequestMapping(value = "/upload")
    public String upload(MultipartFile file) {
        System.out.println("....");
        String filename = file.getOriginalFilename();
        return filename;
    }
}