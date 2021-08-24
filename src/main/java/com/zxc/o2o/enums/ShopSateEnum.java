package com.zxc.o2o.enums;

public enum ShopSateEnum {
    CHECK(0,"审核中"),
    OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),
    NULL_SHOPID(-1002,"shopId为空"),
    NULL_SHOP(-1003,"shop信息为空");
    private int state;
    private String stateInfo;

    //定义为private是不希望三方程序改变Enum值
    private ShopSateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    //依据传入的state返回相应的enum值
    public static ShopSateEnum stateOf(int state){
        for (ShopSateEnum sateEnum : values()){
            if (sateEnum.getState() == state){
                return sateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
