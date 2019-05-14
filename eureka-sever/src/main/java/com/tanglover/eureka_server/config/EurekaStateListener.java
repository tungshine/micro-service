package com.tanglover.eureka_server.config;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-14 10:02
 * @description:
 */
//@Component
//@ComponentScan
@Configuration
public class EurekaStateListener {

    private final static Logger logger = LoggerFactory.getLogger(EurekaStateListener.class);

//    @Autowired
//    private JavaMailSender jms;

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        String msg = "服务：" + event.getAppName() + " " + event.getServerId() + "已下线";
        logger.info(msg);
//        this.send(msg);
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String msg = "服务：" + instanceInfo.getAppName() + " " + instanceInfo.getHostName() + ":" + instanceInfo.getPort() + " ip: " + instanceInfo.getIPAddr() + "进行注册";
        logger.info(msg);
//        this.send(msg);

    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        logger.info("服务：{}进行续约", event.getServerId() + "  " + event.getAppName());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        logger.info("注册中心启动,{}", System.currentTimeMillis());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        logger.info("注册中心服务端启动,{}", System.currentTimeMillis());
    }

//    private void send(String msg) {
//        //用于封装邮件信息的实例
//        SimpleMailMessage smm = new SimpleMailMessage();
//        //由谁来发送邮件
//        smm.setFrom("362615233@qq.com");
//        //邮件主题
//        smm.setSubject("Eureka-Server");
//        //邮件内容
//        smm.setText(msg);
//        //接受邮件
//        smm.setTo(new String[]{"281944469@qq.com"});
//        try {
//            jms.send(smm);
//        } catch (Exception e) {
//            logger.info(msg + "错误", e);
//        }
//    }
}