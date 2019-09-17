package com.tanglover.security.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2019-09-17 16:49
 * @description: 系统用户表
 */
@Getter
@Setter
public class SysUser {

    private long sys_user_id;
    private String account;
    private String no;
    private int gender;
    private String nickname;
    private int enabled;
    private String truename;
    private String remark;
    private long creator;
    private long create_time;
    private long modifier;
    private long modify_time;
}