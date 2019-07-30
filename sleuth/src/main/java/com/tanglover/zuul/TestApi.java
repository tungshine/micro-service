package com.tanglover.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TangXu
 * @create 2019-07-30 14:12
 * @description:
 */
@RestController
public class TestApi {

    private static Logger logger = LoggerFactory.getLogger(TestApi.class);

    @RequestMapping("/")
    public String home() {
        return "hello";
    }
}