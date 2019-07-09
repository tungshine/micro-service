package com.tanglover.backstage.model;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-07-09 14:06
 * @description:
 */
@Aspect
@Component
public class PersonAspect {

    private Logger logger = LoggerFactory.getLogger(PersonAspect.class);

    @Pointcut("@annotation(com.tanglover.backstage.model.annotation.PersonAnnotation)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        logger.info("PersonAspect ==> before method : {}", joinPoint.getClass());

    }

    @After("pointCut()")
    public void after(JoinPoint joinPoint) {
        logger.info("PersonAspect ==> after method : {}", joinPoint.getClass());
    }
}