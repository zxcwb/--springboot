package com.zxc.o2o.dto;

import com.zxc.o2o.entity.Product;
import com.zxc.o2o.enums.ProductStateEnum;
import lombok.Data;

import java.util.List;

@Data
public class ProductExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //商品数量
    private int count;

    //操作的product（增删改商品的时候使用）
    private Product product;

    //获取的product列表（查询商品列表的时候用）
    private List<Product> productList;

    public ProductExecution() {
    }

    //失败时使用的构造器
    public ProductExecution(ProductStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //成功时使用的构造器
    public ProductExecution(ProductStateEnum stateEnum, Product product){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.product = product;
    }

    //成功时使用的构造器
    public ProductExecution(ProductStateEnum stateEnum,List<Product> productList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productList = productList;
    }
}
