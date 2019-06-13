package com.tanglover.mq;

import com.tanglover.mq.config.HelloSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author TangXu
 * @create 2019-05-30 16:35
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MqTest {

    @Autowired
    HelloSender sender;

    @Test
    public void test() {
        sender.send();
    }
}