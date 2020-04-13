package com.tanglover.mall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2020-04-05 14:13
 * @description: 线下门店
 */
@Getter
@Setter
public class Store {

    private long id;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 店铺地址
     */
    private String address;
    /**
     * 联系电话
     */
    private String telPhone;
    /**
     * 详情
     */
    private String details;
}