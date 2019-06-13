package com.tanglover.mq;

import com.tanglover.mq.topic.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author TangXu
 * @create 2019-05-30 17:41
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {

    @Autowired
    private TopicSender sender;

    @Test
    public void topic() throws Exception {
        sender.send();
    }

    @Test
    public void topic1() throws Exception {
        sender.send1();
    }

    @Test
    public void topic2() throws Exception {
        sender.send2();
    }
}