package com.tanglover.backstage.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tanglover.backstage.model.American;
import com.tanglover.backstage.model.Chinese;
import com.tanglover.backstage.service.CglibService;
import com.tanglover.backstage.service.EmpService;
import com.tanglover.starter.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author TangXu
 * @Description
 * @Date 2019/7/9 11:17
 */
@RestController
public class EmpApi extends BaseApi {

    private final Logger logger = LoggerFactory.getLogger(EmpApi.class);

    @Autowired
    EmpService empService;
    @Autowired
    CglibService cglibService;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Map<String, Object> add() {
        return returnSuccess("1");
    }

    @Autowired
    private PersonService personService;

//    @RequestMapping("/test")
//    public String test() {
//        return personService.sayHello();
//    }

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

    @RequestMapping("/testCglib")
    public String testCglib() {
        logger.info("testCglib ===> invokeMethod:{}", empService.getClass());
        logger.info("testCglib ===> invokeMethod:{}", cglibService.getClass());
        return empService.getEmp();
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