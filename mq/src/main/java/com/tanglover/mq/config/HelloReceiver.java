package com.tanglover.mq.config;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-30 16:33
 * @description:
 */
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("Receiver: " + msg);
    }
}