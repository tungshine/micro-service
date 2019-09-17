package com.tanglover.security.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2019-09-17 16:53
 * @description: 角色表
 */
@Getter
@Setter
public class SysRole {

    private long sys_role_id;
    private String name;
    private String description;
    private int enabled;
    private long creator;
    private long create_time;
    private long modifier;
    private long modify_time;
}