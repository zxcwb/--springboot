package com.zxc.o2o.dto;

import com.zxc.o2o.entity.UserProductMap;
import com.zxc.o2o.enums.UserProductMapStateEnum;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 22:12
 * @Version 1.0
 *
 */
public class UserProductMapExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    // 授权数
    private Integer count;

    // 操作的UserProductMap
    private UserProductMap UserProductMap;

    // 授权列表（查询专用）
    private List<UserProductMap> UserProductMapList;

    public UserProductMapExecution() {
    }

    // 失败的构造器
    public UserProductMapExecution(UserProductMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public UserProductMapExecution(UserProductMapStateEnum stateEnum,
                                UserProductMap UserProductMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.UserProductMap = UserProductMap;
    }

    // 成功的构造器
    public UserProductMapExecution(UserProductMapStateEnum stateEnum,
                                List<UserProductMap> UserProductMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.UserProductMapList = UserProductMapList;
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

    public UserProductMap getUserProductMap() {
        return UserProductMap;
    }

    public void setUserProductMap(UserProductMap UserProductMap) {
        this.UserProductMap = UserProductMap;
    }

    public List<UserProductMap> getUserProductMapList() {
        return UserProductMapList;
    }

    public void setUserProductMapList(List<UserProductMap> UserProductMapList) {
        this.UserProductMapList = UserProductMapList;
    }
}
