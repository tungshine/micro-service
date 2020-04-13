package com.tanglover.mall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2020-04-05 14:34
 * @description: 会员
 */
@Getter
@Setter
public class Member {

    private long id;
    private String username;
    private String password;
    private String telPhone;
    private String nickname;
    private long registerTime;

}