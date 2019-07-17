package com.tanglover.mall.aop;

import com.tanglover.mall.aop.annotation.PersonAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-07-09 14:06
 * @description:
 */
@Component
public class Chinese implements Person {
    private Logger logger = LoggerFactory.getLogger(Person.class);

    public Chinese() {
        super();
        logger.info("Chinese ==> Chinese method : 正在生成一个Chinese实例");
    }

    @Override
    @PersonAnnotation(name = "Chinese")//该注解是用来定义切点
    public String say(String name) {
        logger.info("Chinese ==> say method : say {}", name);
        return name + " hello, JDK implement AOP";
    }

    @Override
    public void eat(String food) {
        logger.info("Chinese ==> eat method : eat {}", food);
    }
}