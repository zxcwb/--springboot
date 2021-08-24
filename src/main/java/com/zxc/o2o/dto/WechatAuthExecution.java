package com.zxc.o2o.dto;

import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.enums.WechatAuthStateEnum;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 7:11
 * @Version 1.0
 *
 */
public class WechatAuthExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    private int count;

    private WechatAuth wechatAuth;

    private List<WechatAuth> wechatAuthList;

    private WechatAuthExecution(){ }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WechatAuth getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(WechatAuth wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public List<WechatAuth> getWechatAuthList() {
        return wechatAuthList;
    }

    public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
        this.wechatAuthList = wechatAuthList;
    }

    //失败时使用的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //成功时使用的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum, WechatAuth wechatAuth){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.wechatAuth = wechatAuth;
    }

    //成功时使用的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum,List<WechatAuth> wechatAuthList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.wechatAuthList = wechatAuthList;
    }
}
