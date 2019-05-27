package com.tanglover.sso.service;

import com.tanglover.sso.jdbc.bean.SysUser;
import com.tanglover.sso.jdbc.dao.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @author TangXu
 * @create 2019-05-27 11:26
 * @description:
 */
@Service
public class UserService {

    @Autowired
    SysUserDao sysUserDao;

    @Transactional
    public long insertMaster(SysUser user) throws SQLException {
        long insert = sysUserDao.insert(user);
        return insert;
    }

    @Transactional
    public long insertSlave(SysUser user) throws SQLException {
        long insert = sysUserDao.insert(user);
        return insert;
    }
}