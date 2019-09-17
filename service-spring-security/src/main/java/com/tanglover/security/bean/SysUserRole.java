package com.tanglover.security.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2019-09-17 16:54
 * @description: 用戶角色表
 */
@Getter
@Setter
public class SysUserRole {

    private long sys_user_role_id;
    private long sys_user_id;
    private long sys_role_id;
    private int enabled;
    private long create_time;
}