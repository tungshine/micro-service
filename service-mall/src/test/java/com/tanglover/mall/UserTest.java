package com.tanglover.mall;

import com.alibaba.fastjson.JSONObject;
import com.tanglover.mall.utils.HttpRequestUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-11-22 14:09
 * @description:
 */
public class UserTest {

    static String host = "http://localhost:7074";

    @Test
    public void testQueryBankcard() {
        Map<String, Object> data = new HashMap<String, Object>();
        String token = "4E9B87659B4146D09DC27E0A3790FF3E";
        data.put("timestamp", "1574318918000");
        data.put("token", token);
        data.put("source", "1");
        data.put("investorId", 15);
        System.out.println(HttpRequestUtil.doPost(host + "/products", JSONObject.toJSONString(data)));
    }
}