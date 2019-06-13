package com.tanglover.mq.model;

import java.io.Serializable;

/**
 * @author TangXu
 * @create 2019-05-30 17:31
 * @description:
 */
public class User implements Serializable {

    private String name;
    private String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}