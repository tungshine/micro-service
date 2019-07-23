package com.tanglover.zuul.elk;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: TangXu
 * @Date: 2019/7/23 17:54
 * @Description:
 */
@Setter
@Getter
public class BaseMessageModel {
    /**
     * 项目名称
     */
    private String project;
    /**
     * 来源IP
     */
    private String IP;
    /**
     * 请求接口URL
     */
    private String requestUri;
    /**
     * 请求参数JSON
     */
    private JSONObject requestJSON;
    /**
     * 返回数据JSON
     */
    private JSONObject responseJSON;

}
