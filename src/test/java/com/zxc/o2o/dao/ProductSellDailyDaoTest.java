package com.zxc.o2o.dao;

import com.zxc.o2o.entity.Product;
import com.zxc.o2o.entity.ProductSellDaily;
import com.zxc.o2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 21:01
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSellDailyDaoTest {

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void insertProductSellDaily() {
        int result  = productSellDailyDao.insertProductSellDaily();
        System.out.println(result);
    }

    @Test
    public void queryProductSellDailyList() {
        productSellDailyDao.insertDefaultProductSellDaily();
    }

}