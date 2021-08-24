package com.zxc.o2o.dto;

import com.zxc.o2o.entity.LocalAuth;
import com.zxc.o2o.enums.LocalAuthStateEnum;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 11:30
 * @Version 1.0
 *
 */
public class LocalAuthExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    private int count;

    private LocalAuth localAuth;

    private List<LocalAuth> localAuthList;

    private LocalAuthExecution(){ }

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

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    public List<LocalAuth> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<LocalAuth> localAuthList) {
        this.localAuthList = localAuthList;
    }

    //失败时使用的构造器
    public LocalAuthExecution(LocalAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //成功时使用的构造器
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, LocalAuth localAuth){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuth = localAuth;
    }

    //成功时使用的构造器
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, List<LocalAuth>localAuthList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuthList = localAuthList;
    }
}
