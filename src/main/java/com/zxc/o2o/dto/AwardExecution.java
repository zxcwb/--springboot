package com.zxc.o2o.dto;

import com.zxc.o2o.entity.Award;
import com.zxc.o2o.enums.AwardStateEnum;
import lombok.Data;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 10:17
 * @Version 1.0
 *
 */
@Data
public class AwardExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的Award（增删改店铺时候用）
    private Award award;

    //Award列表(查询店铺列表的时候使用)
    public List<Award> awardList;

    public AwardExecution() {
    }

    //店铺操作失败的时候使用的构造器
    public AwardExecution(AwardStateEnum sateEnum){
        this.state = sateEnum.getState();
        this.stateInfo = sateEnum.getStateInfo();
    }

    //店铺操作成功的时候使用的构造器
    public AwardExecution(AwardStateEnum sateEnum, Award award){
        this.state = sateEnum.getState();
        this.stateInfo = sateEnum.getStateInfo();
        this.award = award;
    }

    //店铺操作成功的时候使用的构造器
    public AwardExecution(AwardStateEnum sateEnum, List<Award> awardList){
        this.state = sateEnum.getState();
        this.stateInfo = sateEnum.getStateInfo();
        this.awardList = awardList;
    }
}
