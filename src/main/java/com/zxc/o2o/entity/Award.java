package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/7 17:46
 * @Version 1.0
 * 奖品
 */
@Data
public class Award {
    //主键ID
    private Long awardId;
    //奖品名
    private String awardName;
    //奖品描述
    private String awardDesc;
    //奖品图片地址
    private String awardImg;
    //需要多少积分去兑换
    private Integer point;
    //权重
    private Integer priority;
    //创建时间
    private Date createTime;
    //过期时间
    private Date expireTime;
    //最近一次更新时间
    private Date lastEditTime;
    //可用状态0：不可用，1：可用
    private Integer enableStatus;
    //属于哪个店铺
    private Long ShopId;
}
