package com.tanglover.security.service;

import com.tanglover.security.jdbc.bean.SysUser;
import com.tanglover.security.jdbc.dao.SysUserDao;
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

    @Transactional(rollbackFor = Exception.class)
    public long insertMaster(SysUser user) throws SQLException {
        long insert = sysUserDao.insert(user);
        return insert;
    }

    @Transactional(rollbackFor = Exception.class)
    public long insertSlave(SysUser user) throws SQLException {
        long insert = sysUserDao.insert(user);
        return insert;
    }
}