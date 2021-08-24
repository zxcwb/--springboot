package com.zxc.o2o.dto;

import com.zxc.o2o.entity.UserAwardMap;
import com.zxc.o2o.enums.UserAwardMapStateEnum;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 8:23
 * @Version 1.0
 *
 */
public class UserAwardMapExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    // 授权数
    private Integer count;

    // 操作的UserAwardMap
    private com.zxc.o2o.entity.UserAwardMap UserAwardMap;

    // 授权列表（查询专用）
    private List<UserAwardMap> UserAwardMapList;

    public UserAwardMapExecution() {
    }

    // 失败的构造器
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum,
                                UserAwardMap UserAwardMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.UserAwardMap = UserAwardMap;
    }

    // 成功的构造器
    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum,
                                List<UserAwardMap> UserAwardMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.UserAwardMapList = UserAwardMapList;
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

    public UserAwardMap getUserAwardMap() {
        return UserAwardMap;
    }

    public void setUserAwardMap(UserAwardMap UserAwardMap) {
        this.UserAwardMap = UserAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return UserAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> UserAwardMapList) {
        this.UserAwardMapList = UserAwardMapList;
    }
}
