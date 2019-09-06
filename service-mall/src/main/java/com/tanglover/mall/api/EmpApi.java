package com.tanglover.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tanglover.mall.aop.American;
import com.tanglover.mall.aop.Chinese;
import com.tanglover.mall.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author TangXu
 * @Description
 * @Date 2019/7/9 11:17
 */
@RestController
public class EmpApi extends BaseApi {

    private final Logger logger = LoggerFactory.getLogger(EmpApi.class);
    private final static String UNKNOWN = "unknown";

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Map<String, Object> add(HttpServletRequest request) {
        JSONObject jsonObject = HttpUtils.getJSONObject(request);
        logger.info(jsonObject.toJSONString());
        return returnSuccess("1");
    }

    @RequestMapping("/hi")
    @HystrixCommand(fallbackMethod = "hiErr")
    public String hi(@RequestParam String name) {
        return "hi," + name + "I am a port:" + port;
    }

    @RequestMapping("/hiErr")
    public String hiErr(@RequestParam String name) {
        return "sorry," + name + ",have a error";
    }

    @Autowired
    private American american;
    @Autowired
    private Chinese chinese;

    @RequestMapping("/cglib")
    public String cglib() {
        chinese.say("中国人说汉语");
        american.say("American say english");
        logger.info("EmpApi ==> talk method : {}", chinese.getClass());
        logger.info("EmpApi ==> talk method : {}", american.getClass());
        return "success";
    }

}