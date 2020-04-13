package com.tanglover.mall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2020-04-05 14:36
 * @description: 订单
 */
@Getter
@Setter
public class Order {

    private long id;
    private long memberId;
    private long flowerId;
    /**
     * 购买数量
     */
    private int count;
    /**
     * 署名
     */
    private String signature;
    /**
     * 祝福语
     */
    private String blessings;
    /**
     * 优惠券状态（0：未使用、1：已使用）
     */
    private int couponState;
    /**
     * 优惠券id
     */
    private long couponId;
    /**
     * 订单价格
     */
    private long orderPrice;
    private long orderTime;
    private int orderState;

}