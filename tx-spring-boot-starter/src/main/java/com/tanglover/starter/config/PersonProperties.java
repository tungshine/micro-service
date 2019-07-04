package com.tanglover.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author TangXu
 * @create 2019-06-24 17:11
 * @description:
 */
@ConfigurationProperties(prefix = "spring.person")
public class PersonProperties {
    /**
     * name
     */
    private String name;
    /**
     * age
     */
    private int age;
    /**
     * sex
     */
    private String sex = "M";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}