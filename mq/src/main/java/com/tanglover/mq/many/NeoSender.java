package com.tanglover.mq.many;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-30 17:44
 * @description:
 */
@Component
public class NeoSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(int i) {
        String context = "spring boot neo queue" + " ****** " + i;
        System.out.println("Sender1 : " + context);
        this.rabbitTemplate.convertAndSend("neo", context);
    }
}