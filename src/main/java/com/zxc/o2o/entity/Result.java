package com.zxc.o2o.entity;

import lombok.Data;

@Data
public class Result<T> {
    //是否成功
    private boolean success;

    //成功时返回的数据
    private T data;

    //错误的信息
    private String errorMsg;

    //状态码
    private int errorCode;

    public Result(){

    }

    //成功的构造器
    public Result(boolean success,T data){
        this.success = success;
        this.data = data;
    }

    //错误时的构造器
    public Result(boolean success, String errorMsg, int errorCode) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }


}
