package com.tanglover.security.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TangXu
 * @create 2020-04-28 13:49
 * @description:
 */
@RestController
public class FileApi extends BaseApi {


    @PostMapping("/upload")
    public Map upload(HttpServletRequest request) {

        return returnSuccess(0);
    }
}