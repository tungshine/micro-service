package com.tanglover.mq.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author TangXu
 * @create 2019-05-30 16:30
 * @description:
 */
@Component
public class HelloSender {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        String context = "hello " + sdf.format(new Date());
        System.out.println("Sender: " + context);
        this.amqpTemplate.convertAndSend("hello", context);
    }

    public void sendMessage() {
        String context = "hi, i'm message 1";
        System.out.println("Sender: " + context);
        this.amqpTemplate.convertAndSend("exchange", "topic.message", context);
    }

    public void sendMessages() {
        String context = "hi, i'm message 2";
        System.out.println("Sender: " + context);
        this.amqpTemplate.convertAndSend("exchange", "topic.messages", context);
    }

}