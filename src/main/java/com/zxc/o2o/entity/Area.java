package com.zxc.o2o.entity;
//区域实体类

import lombok.Data;

import java.util.Date;

@Data //使用lombok生成setter、getter等方法
public class Area {
    //ID
    private Integer areaId;
    //名称
    private String areaName;
    //权重(优先级)
    private Integer priority;
    //创建时间
    private Date createTime;
    //更新时间
    private Date lastEnditTime;

    public Area() {
    }

}
