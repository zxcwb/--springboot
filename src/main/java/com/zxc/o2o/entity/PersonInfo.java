package com.zxc.o2o.entity;

import lombok.Data;

import java.util.Date;

//个人信息
@Data
public class PersonInfo {
    private Long userId;
    private String name;
    //头像地址
    private String profileImg;
    //用户邮箱
    private String email;
    //性别
    private String gender;
    //用户状态
    private Integer enableStatus;
    //顾客、店家、超级管理员
    private Integer userType;
    private Date createTime;
    private Date lastEditTime;
}
