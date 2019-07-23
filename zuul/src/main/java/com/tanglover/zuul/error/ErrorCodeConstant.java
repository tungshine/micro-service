package com.tanglover.zuul.error;

/**
 * @Author: TangXu
 * @Date: 2018/6/1 10:54
 * @Description: 错误代码定义
 */
public enum ErrorCodeConstant {


    ERROR_INTERCEPTOR(500, "The state of the network is not good", "拦截后错误统一返回代码");

    /**
     * 代码
     */
    public long code;
    /**
     * 消息内容
     */
    public String message;
    /**
     * 实例含义
     */
    public String intro;

    ErrorCodeConstant() {
        this(500, "The state of the network is not good", "拦截后错误统一返回代码");
    }

    ErrorCodeConstant(long code, String message, String intro) {
        this.code = code;
        this.message = message;
        this.intro = intro;
    }
}
