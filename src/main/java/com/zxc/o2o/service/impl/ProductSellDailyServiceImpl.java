package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.ProductSellDailyDao;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.entity.ProductSellDaily;
import com.zxc.o2o.service.ProductSellDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 20:44
 * @Version 1.0
 *
 */
@Service
@Slf4j
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        //继续日销量定时统计
        log.info("QuartZ Running");
        //统计在tb_user_product_map里面产生销量的每个店铺的每件商品的日销量
        productSellDailyDao.insertProductSellDaily();
        //统计余下的商品的日销量，全部置为0（为了迎合echarts的数据请求）
        productSellDailyDao.insertDefaultProductSellDaily();
    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition,beginTime,endTime);
    }
}
