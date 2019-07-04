package com.tanglover.starter.service;

import com.tanglover.starter.config.PersonProperties;

/**
 * @author TangXu
 * @create 2019-06-24 17:12
 * @description:
 */
public class PersonService {

    private PersonProperties personProperties;

    public PersonService() {
    }

    public PersonService(PersonProperties properties) {
        this.personProperties = properties;
    }

    public String sayHello() {
        String msg = "大家好，我叫: " + personProperties.getName() + ", 今年" + personProperties.getAge() + "岁"
                + ", 性别: " + personProperties.getSex();
        System.out.println(msg);
        return msg;
    }
}