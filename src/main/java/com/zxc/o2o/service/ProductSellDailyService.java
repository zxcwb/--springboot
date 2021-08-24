package com.zxc.o2o.service;

import com.zxc.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 20:43
 * @Version 1.0
 *
 */
public interface ProductSellDailyService {
    /*
    * 每日定时对所有的店铺的商品销售进行统计
    * */
    void dailyCalculate();

    /*
    * 根据查询条件返回商品日销售的统计列表
    * */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,Date endTime);
}
