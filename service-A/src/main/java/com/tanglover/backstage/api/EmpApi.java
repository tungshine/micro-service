package com.tanglover.backstage.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tanglover.starter.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class EmpApi extends BaseApi {

    private final Logger logger = LoggerFactory.getLogger(EmpApi.class);

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Map<String, Object> add() {
        return returnSuccess("1");
    }

    @Autowired
    private HelloService helloService;

    @RequestMapping("/test")
    public String test() {
        return helloService.say("tang");
    }

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hi")
    @HystrixCommand(fallbackMethod = "hiErr")
    public String hi(@RequestParam String name) {
        return "hi," + name + "I am a port:" + port;
    }

    @RequestMapping("/hiErr")
    public String hiErr(@RequestParam String name) {
        return "sorry," + name + ",have a error";
    }

}