package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;

//本地账号
@Data
public class LocalAuth {
    private Long localAuthId;
    private Long userId;
    private String userName;
    private String password;
    private Integer userType;
    private Date createTime;
    private Date lastEditTime;
    private PersonInfo personInfo;
}
