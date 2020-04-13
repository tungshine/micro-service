package com.tanglover.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.tanglover.mall.service.UserService;
import com.tanglover.mall.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-07-16 15:02
 * @description:
 */
//@RestController
public class UserApi extends BaseApi {

    @Autowired
    private UserService userService;

    @RequestMapping("/buy")
    public Map<String, Object> buy(HttpServletRequest request) {
        JSONObject reqJson = HttpUtils.getJSONObject(request);
        if (null == reqJson) {
            return returnError(10001, "参数错误");
        }
        long userId = reqJson.getLongValue("userId");
        if (0 == userId) {
            return returnError(10001, "参数错误");
        }
        long productId = reqJson.getLongValue("productId");
        if (0 == productId) {
            return returnError(10001, "参数错误");
        }
        long count = reqJson.getLongValue("count");
        Map<String, Object> grab = userService.grabForRedis(userId, productId, count);
        return returnSuccess(grab);
    }
}