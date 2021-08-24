package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;

//微信账号
@Data
public class WechatAuth {
    private Long wechatAuthId;
    private String openId;
    private Date createTime;
    private PersonInfo personInfo;
}
