package com.tanglover.security.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TangXu
 * @create 2019-08-22 11:17
 * @description:
 */
@Controller
public class LoginApi extends BaseApi {

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "login";
    }
}