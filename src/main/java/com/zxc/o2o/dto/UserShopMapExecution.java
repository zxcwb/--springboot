package com.zxc.o2o.dto;

import com.zxc.o2o.entity.UserShopMap;
import com.zxc.o2o.enums.UserShopMapStateEnum;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 21:30
 * @Version 1.0
 *
 */
public class UserShopMapExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    // 授权数
    private Integer count;

    // 操作的UserShopMap
    private UserShopMap UserShopMap;

    // 授权列表（查询专用）
    private List<UserShopMap> UserShopMapList;

    public UserShopMapExecution() {
    }

    // 失败的构造器
    public UserShopMapExecution(UserShopMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public UserShopMapExecution(UserShopMapStateEnum stateEnum,
                                   UserShopMap UserShopMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.UserShopMap = UserShopMap;
    }

    // 成功的构造器
    public UserShopMapExecution(UserShopMapStateEnum stateEnum,
                                   List<UserShopMap> UserShopMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.UserShopMapList = UserShopMapList;
    }

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserShopMap getUserShopMap() {
        return UserShopMap;
    }

    public void setUserShopMap(UserShopMap UserShopMap) {
        this.UserShopMap = UserShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return UserShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> UserShopMapList) {
        this.UserShopMapList = UserShopMapList;
    }
}
