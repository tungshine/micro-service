package com.tanglover.captcha.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TangXu
 * @create 2019-11-20 15:09
 * @description:
 */
@RestController
public class CaptchaController {

    @RequestMapping("/test")
    public void test(HttpServletRequest request) {

    }
}