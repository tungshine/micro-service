package com.tanglover.backstage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author TangXu
 * @create 2019-07-09 10:59
 * @description:
 */
@Service
public class EmpServiceImpl implements EmpService {

    Logger logger = LoggerFactory.getLogger(EmpServiceImpl.class);

    @Override
    public String getEmp() {
//        logger.info("验证是否是cglib");
        return "emp";
    }
}