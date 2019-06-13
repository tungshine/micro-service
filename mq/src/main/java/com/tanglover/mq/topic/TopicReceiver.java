package com.tanglover.mq.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-30 17:32
 * @description:
 */
@Component
@RabbitListener(queues = "topic.message")
public class TopicReceiver {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("Topic Receiver: " + msg);
    }
}