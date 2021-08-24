package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/8 7:10
 * @Version 1.0
 * 店铺授权
 */
@Data
public class ShopAuthMap {
    //主键Id
    private Long ShopAuthId;
    //职称名
    private String title;
    //职称符号 (可用于去权限控制)
    private Integer titleFlag;
    //授权有效状态，0：无效，1：有效
    private Integer enableStatus;
    //创建时间
    private Date createTime;
    //最近更新一次时间
    private Date lastEditTime;
    //员工信息实体类
    private PersonInfo employee;
    //店铺实体类
    private Shop shop;

}
