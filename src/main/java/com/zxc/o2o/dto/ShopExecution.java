package com.zxc.o2o.dto;

import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.enums.ShopSateEnum;
import lombok.Data;

import java.util.List;

@Data
public class ShopExecution {
    
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop（增删改店铺时候用）
    private Shop shop;

    //shop列表(查询店铺列表的时候使用)
    public List<Shop> shopList;

    public ShopExecution() {
    }

    //店铺操作失败的时候使用的构造器
    public ShopExecution(ShopSateEnum sateEnum){
        this.state = sateEnum.getState();
        this.stateInfo = sateEnum.getStateInfo();
    }

    //店铺操作成功的时候使用的构造器
    public ShopExecution(ShopSateEnum sateEnum, Shop shop){
        this.state = sateEnum.getState();
        this.stateInfo = sateEnum.getStateInfo();
        this.shop = shop;
    }

    //店铺操作成功的时候使用的构造器
    public ShopExecution(ShopSateEnum sateEnum, List<Shop> shopList){
        this.state = sateEnum.getState();
        this.stateInfo = sateEnum.getStateInfo();
        this.shopList = shopList;
    }

}
