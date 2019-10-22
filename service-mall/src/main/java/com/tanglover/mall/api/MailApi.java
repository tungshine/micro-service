package com.tanglover.mall.api;

import com.tanglover.mall.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author TangXu
 * @create 2019-10-22 16:55
 * @description:
 */
@RestController
public class MailApi {

    @Autowired
    private MailService mailService;

    @RequestMapping("/testSendMail")
    public Map<String, Object> testSendMail() {
        mailService.setMailSender("", "", "");
        return null;
    }
}