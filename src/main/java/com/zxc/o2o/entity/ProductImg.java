package com.zxc.o2o.entity;

//详情图片

import lombok.Data;

import java.util.Date;

@Data
public class ProductImg {
    private Long productImgId;
    private String imgAddr;
    private String imgDesc;
    private Integer priority;
    private Date createTime;
    private Long productId;

}
