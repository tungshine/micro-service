package com.tanglover.security.jdbc.dao;

import com.tanglover.security.jdbc.bean.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
//SysUser

@Repository
public class SysUserDao {

    Logger logger = LoggerFactory.getLogger(SysUserDao.class);

    @Resource(name = "masterJdbcTemplate")
    private JdbcTemplate masterJdbcTemplate;

    @Resource(name = "slaveJdbcTemplate")
    private JdbcTemplate slaveJdbcTemplate;


    public long insert(SysUser user) {
        String sql = "INSERT INTO test.sys_user(account, password, gender, nickname, create_time, modify_time) values (?, ?, ?, ?, ?, ?)";
        return masterJdbcTemplate.update(sql, new Object[]{user.getAccount(), user.getPassword(), user.getGender(), user.nickname, System.currentTimeMillis(), System.currentTimeMillis()});
    }

    public List<SysUser> getUsers() {
        String sql = "select * from sys_user";
        List<SysUser> users = masterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysUser.class));
        return users;
    }

}
