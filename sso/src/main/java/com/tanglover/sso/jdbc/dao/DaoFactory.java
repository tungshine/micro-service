package com.tanglover.sso.jdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//DAO Factory
@Repository("daoFactory")
public class DaoFactory {

    @Autowired
    protected SysUserDao sysUserDaoDao;

    /*******************************下面是GET方法**************************************/
    public SysUserDao getSysUserDao() {
	    return sysUserDaoDao;
    }

}
