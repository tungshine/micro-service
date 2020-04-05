package com.tanglover.mall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2019-07-16 15:05
 * @description:
 */
@Getter
@Setter
public class User {

    private long id;
    private String username;
    private String password;
    private String birthday;
    private long registerTime;
    private long firstLoginTime;

}