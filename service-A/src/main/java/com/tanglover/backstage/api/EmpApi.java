package com.tanglover.backstage.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class EmpApi extends BaseApi {

    private final Logger logger = LoggerFactory.getLogger(EmpApi.class);

    @Autowired
    private DiscoveryClient client;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Map<String, Object> add() {
        return returnSuccess("1");
    }
//
//    //A服务调用B服务
//    @RequestMapping(value = "testServiceB", method = RequestMethod.GET)
//    public String testServiceB(@RequestParam Integer a, @RequestParam Integer b) {
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate.getForObject("http://localhost:7075/add?a=" + a + "&b=" + b, String.class);
//    }

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hi")
    @HystrixCommand(fallbackMethod = "hiErr")
    public String hi(@RequestParam String name) {
        return "hi," + name + "I am a port:" + port;
    }

    public String hiErr(@RequestParam String name) {
        return "sorry," + name + ",have a error";
    }

}