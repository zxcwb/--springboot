package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

//商品
@Data
public class Product {
    //商品Id
    private Long productId;
    //商品名称
    private String productName;
    //描述
    private String productDesc;
    //缩略图地址
    private String imgAddr;
    //原价
    private String normalPrice;
    //折扣价
    private String promotionPrice;
    //权重
    private Integer priority;
    //商品积分
    private Integer point;
    //创建时间
    private Date createTime;
    //修改时间
    private Date lastEditTime;
    //0、下架 1、在前端展示系统展示
    private Integer enableStatus;
    //商品详情图片列表
    private List<ProductImg> productImgList;
    //商品类别
    private ProductCategory productCategory;
    //店铺
    private Shop shop;
}
