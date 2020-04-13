package com.tanglover.mall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2020-04-05 14:04
 * @description: 花
 */
@Getter
@Setter
public class Flower {
    private long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private int type;
    /**
     * 标签
     */
    private String label;
    /**
     * 详情
     */
    private String details;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 原价（以分为单位）
     */
    private long originalPrice;
    /**
     * 折扣（按百分比存取，例如：95折存95）
     */
    private int discount;
    /**
     * 现价（以分为单位）
     */
    private long presentPrice;
    /**
     * 门店id
     */
    private long storeId;

}