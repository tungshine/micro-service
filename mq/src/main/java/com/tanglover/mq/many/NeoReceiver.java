package com.tanglover.mq.many;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-30 17:45
 * @description:
 */
@Component
@RabbitListener(queues = "neo")
public class NeoReceiver {

    @RabbitHandler
    public void process(String neo) {
        System.out.println("Receiver 1: " + neo);
    }
}