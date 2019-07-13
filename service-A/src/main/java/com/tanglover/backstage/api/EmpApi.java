package com.tanglover.backstage.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tanglover.backstage.model.American;
import com.tanglover.backstage.model.Chinese;
import com.tanglover.backstage.service.CglibService;
import com.tanglover.backstage.service.EmpService;
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

    @Autowired
    EmpService empService;
    @Autowired
    CglibService cglibService;

    @RequestMapping("/loadBalance")
    public Map<String, Object> loadBalance(HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        return returnSuccess(ipAddress);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Map<String, Object> add() {
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

    public String getIpAddress(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("X-Forwarded-For");
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (null == ip || 0 == ip.length() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception ignored) {
        }
        return ip == null ? "" : ip;
    }

}