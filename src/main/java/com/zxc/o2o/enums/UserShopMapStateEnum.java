package com.zxc.o2o.enums;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 21:33
 * @Version 1.0
 *
 */
public enum UserShopMapStateEnum {
    SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "操作失败"), NULL_UserShop_ID(-1002,
            "UserShopId为空"), NULL_UserShop_INFO(-1003, "传入了空的信息");

    private int state;

    private String stateInfo;

    private UserShopMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static UserShopMapStateEnum stateOf(int index) {
        for (UserShopMapStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
