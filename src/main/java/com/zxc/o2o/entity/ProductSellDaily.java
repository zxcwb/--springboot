package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/7 18:04
 * @Version 1.0
 * 顾客消费的商品映射
 */
@Data
public class ProductSellDaily {
    private Integer productSellDailyId;
    //哪天的销量，精确到天
    private Date createTime;
    //销量
    private Integer total;
    //商品信息实体类
    private Product product;
    //店铺信息实体类
    private Shop shop;
}
