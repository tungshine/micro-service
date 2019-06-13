package com.tanglover.mq.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-30 17:34
 * @description:
 */
@Component
@RabbitListener(queues = "topic.messages")
public class TopicReceiver2 {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("Receiver2: " + msg);
    }
}