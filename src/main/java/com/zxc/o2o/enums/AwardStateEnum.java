package com.zxc.o2o.enums;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 10:18
 * @Version 1.0
 *
 */
public enum AwardStateEnum {
    CHECK(0,"操作成功！审核中"),
    OFFLINE(-1,"非法操作"),
    SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),
    NULL_AWARDID(-1002,"AwardId为空"),
    NULL_AWARD(-1003,"Award信息为空");
    private int state;
    private String stateInfo;

    //定义为private是不希望三方程序改变Enum值
    private AwardStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    //依据传入的state返回相应的enum值
    public static AwardStateEnum stateOf(int state){
        for (AwardStateEnum sateEnum : values()){
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
