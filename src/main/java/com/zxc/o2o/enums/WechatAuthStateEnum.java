package com.zxc.o2o.enums;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 7:15
 * @Version 1.0
 *
 */
public enum WechatAuthStateEnum {
    LOGIN_FAIL(-1,"openId输入有误！"),
    SUCCESS(0,"操作成功"),
    NULL_AUTH_INFO(-1006, "注册信息为空");

    private int state;

    private String stateInfo;

    WechatAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
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
}
