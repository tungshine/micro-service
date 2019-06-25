package com.tanglover.starter.service;

/**
 * @author TangXu
 * @create 2019-06-24 17:12
 * @description:
 */
public class HelloService {

    private String prefix;

    private String suffix;

    public HelloService(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String say(String text) {
        return String.format("%s , hi , %s , %s", prefix, text, suffix);
    }

}