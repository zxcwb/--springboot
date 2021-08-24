package com.zxc.o2o.enums;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 8:25
 * @Version 1.0
 *
 */
public enum UserAwardMapStateEnum {
    SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "操作失败"),
    NULL_USERAWARD_ID(-1002, "UserAwardId为空"),
    NULL_USERAWARD_INFO(-1003, "传入了空的信息");

    private int state;

    private String stateInfo;

    private UserAwardMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static UserAwardMapStateEnum stateOf(int index) {
        for (UserAwardMapStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
