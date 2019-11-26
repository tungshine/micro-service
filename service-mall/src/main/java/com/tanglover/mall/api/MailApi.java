package com.tanglover.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.tanglover.mall.service.MailService;
import com.tanglover.mall.utils.HttpUtils;
import com.tanglover.mall.utils.Md5Util;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-10-22 16:55
 * @description:
 */
@RestController
public class MailApi extends BaseApi{

    @Autowired
    private MailService mailService;

    @RequestMapping("/testSendMail")
    public Map<String, Object> testSendMail() {
        mailService.setMailSender("", "", "");
        return null;
    }

    @RequestMapping("/sign")
    public Map<String, Object> sign(HttpServletRequest request) {
        JSONObject json = HttpUtils.getJSONObject(request);
        if (null == json) {
            return returnError(10000, "参数异常");
        }
        String sign = json.getString("sign");
        Integer pageNo = json.getInteger("pageNo");
        String username = json.getString("username");
        return null;
    }
}