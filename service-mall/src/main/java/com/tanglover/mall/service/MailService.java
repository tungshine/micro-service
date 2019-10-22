package com.tanglover.mall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-10-22 16:51
 * @description:
 */
@Component
public class MailService {

    @Value("${spring.mail.username}")
    private String fromUser;

    @Autowired
    private JavaMailSender mailSender;

    public void setMailSender(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("281944469@qq.com");
        message.setSubject("test");
        message.setText("test");
        message.setFrom(fromUser);
        mailSender.send(message);
    }
}