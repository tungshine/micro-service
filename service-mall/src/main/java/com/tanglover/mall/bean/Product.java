package com.tanglover.mall.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TangXu
 * @create 2019-07-16 15:05
 * @description:
 */
@Data
public class Product {
    private long id;
    private String product_no;
    private long stock;
    private long version;
    private long create_time;
    private long modify_time;
}