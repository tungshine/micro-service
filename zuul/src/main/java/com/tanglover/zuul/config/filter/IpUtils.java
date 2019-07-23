package com.tanglover.zuul.config.filter;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @Author: TangXu
 * @Date: 2019/07/22 10:39
 * @Description:
 */
@Component
public class IpUtils {

    Logger logger = LoggerFactory.getLogger(IpUtils.class);

    @Autowired
    RedisCache cacheTool;

    /**
     * 获取IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip == null ? "" : ip;
    }

    /**
     * 获取IP
     */
    public static String getIpAddr2(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception ignored) {
        }
         ip = ip == null ? "" : ip;

         if(ip.indexOf(",") != -1 ){
                return  ip.split(",")[0];
         }
         return  ip;

    }


    /**
     * 获取3段IP
     * @param ip
     * @return
     */
    private  String Gain3IP(String ip){
        String[] ip_array = ip.split("\\.");
        if (ip_array == null || ip_array.length != 4){
            return ip;
        }
        StringJoiner joiner = new StringJoiner(".");
        return joiner.add(ip_array[0]).add(ip_array[1]).add(ip_array[2]).toString();
    }



    /**
     * 返回IP地址段 格式
     */
    public  String getIPRouteASL(String ip) {
        if (!StringUtils.hasLength(ip) || ip.indexOf("/") == -1) {
            return ip;
        }

        String[] ip_array = ip.split("/");
        if (ip_array.length != 2) {
            return ip;
        }

        String mask = ip_array[1].trim();
        String rightip = ip_array[0];
        ip_array = rightip.split("\\.");

        StringJoiner joiner = new StringJoiner(".");

        switch (mask) {
            //192.0.0.0/8
            case "8":
                return joiner.add(ip_array[0]).toString();
            //192.168.0.0/16
            case "16":
                return  joiner.add(ip_array[0]).add(ip_array[1]).toString();
            //192.168.1.0/24
            case "24":
                return joiner.add(ip_array[0]).add(ip_array[1]).add(ip_array[2]).toString();
            default:
                return ip;
        }

    }


    /**
     * 是否在IP白名单中
     * @param ip
     * @param config_ip
     * @return true : 在  false:不在
     */
    public boolean isAllowIp(String ip,String config_ip){
        //如果没有配置IP白名单，就允许通过
        if (!StringUtils.hasLength(config_ip)){
            return false;
        }

        String[] config_ip_array = config_ip.split(",");

        //检查格式如果 还是没有可供筛选的数据 不允许直接通过
        if (config_ip_array == null || config_ip_array.length < 1){
            return false;
        }

        //遍历
        for (String temp_ip : config_ip_array) {
            //解析IP格式
            temp_ip  = getIPRouteASL(temp_ip);
            if (ip.equalsIgnoreCase(temp_ip) || ip.startsWith(temp_ip)){
                logger.info("ip={},在IP白名单中......",ip);
                //在IP白名单中
                return true;
            }
        }

        //不在IP白名单中
        return false;
    }


    /**
     * 是否在IP黑名单中
     * @param ip
     * @param config_ip
     * @return true : 在  false:不在
     */
    public boolean isForbiddenIp(String ip,String config_ip){
        //如果没有配置IP黑名单，就允许通过
        if (!StringUtils.hasLength(config_ip)){
            return false;
        }

        String[] config_ip_array = config_ip.split(",");

        //检查格式如果 还是没有可供筛选的数据 也允许直接通过
        if (config_ip_array == null || config_ip_array.length < 1){
            return false;
        }

        //遍历
        for (String temp_ip : config_ip_array) {
            //解析IP格式
            temp_ip  = getIPRouteASL(temp_ip);
            if (ip.equalsIgnoreCase(temp_ip) || ip.startsWith(temp_ip)){
                logger.error("ip={},在IP黑名单中被拦截......",ip);
                //在IP黑名单中
                return true;
            }
        }

        //不在IP黑名单中
        return false;
    }



    /**
     * 记录访问请求过来的 IP前三段记数累计
     * @return true : 超出了允许次数 把IP加入黑名单 直接拒绝请求
     */
    public  boolean RecordIPAndCount(String ip,Long expireSeconds,int allow_count){
        //Object key, Object value, Long expireSeconds, TimeUnit timeunit

        ip = Gain3IP(ip);

        String key = RedisKeyConstants.getCacheKey(RedisKeyConstants.getIpforbiden_email_prefix(),ip);

        Object count_obj = cacheTool.getObject(key);
        if (StringUtils.isEmpty(count_obj)){
            //如果是第一次记录
            cacheTool.putObject(key,1,expireSeconds, TimeUnit.SECONDS);
        }else{
            int count = Integer.parseInt(count_obj.toString());
            if (count >= allow_count){
                //超出了允许次数 把IP加入黑名单 直接拒绝请求
                return true;
            }

            //如果不是第一次记录 就累计
            cacheTool.putObject(key,count+1,expireSeconds, TimeUnit.SECONDS);
        }
        return false;
    }

    /**
     * 检查是否存在被禁止的邮件类型
     * @return  true : 存在  false:不存在
     */
    public boolean isExistForbiddenEmail(Configuration config, HttpServletRequest request, JSONObject jsonObject){
        try {
//            logger.info("-------allow_email_suffixes={},Forbidden_email_pattern=={}",config.getAllow_email_suffixes(),config.getForbidden_email_pattern());
            if (jsonObject == null || jsonObject.isEmpty()){
                //如果为空JSON对象 直接返回正常流程
                return  false;
            }

            String this_request_uri = request.getRequestURI().replace("/","");

            //对需要筛选的接口进行判定
            String request_uri = config.getForbidden_ips_uri();
            if (!StringUtils.hasLength(request_uri)){
                return false;
            }

            if(request_uri.indexOf(this_request_uri) == -1){
                //该次请求的接口 不在我们限制的接口范围内 直接返回正常流程
                return false;
            }

            //获取邮件
            String email = jsonObject.getString("email");

            //by jxh 2018-7-10 检查是否是允许的邮件后缀类型  true:允许 false:不允许
            if (RegularUtil.isAllowEmilSuffixes(config.getAllow_email_suffixes(),email)){
                //是允许的后缀放行通过 不禁止

                //进行正则表达式检查
                return RegularUtil.isSpiteRegular(email,config.getForbidden_email_pattern());

            }else{
                //被禁止
                return  true;
            }




        } catch (Exception e) {
            return  false;
        }


    }

}
