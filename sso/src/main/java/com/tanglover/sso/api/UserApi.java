package com.tanglover.sso.api;

import com.tanglover.sso.jdbc.bean.SysUser;
import com.tanglover.sso.service.UserService;
import com.tanglover.sso.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-05-27 11:32
 * @description:
 */
@RestController
public class UserApi extends BaseApi {

    @Autowired
    UserService userService;

    @PostMapping("/insertSlave")
    public Map<String, Object> insertSlave(HttpServletRequest request) throws SQLException {
        SysUser user = (SysUser) HttpUtils.parseObject(request, SysUser.class);
        return returnSuccess(userService.insertSlave(user));
    }
}