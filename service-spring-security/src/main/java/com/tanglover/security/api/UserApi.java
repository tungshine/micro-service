package com.tanglover.security.api;

import com.alibaba.fastjson.JSONObject;
import com.tanglover.security.jdbc.bean.SysUser;
import com.tanglover.security.service.UserService;
import com.tanglover.security.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/getUser")
    public Map<String, Object> getUser(HttpServletRequest request) {
        JSONObject jsonObject = HttpUtils.getJSONObject(request);
        return returnSuccess(jsonObject);
    }

}