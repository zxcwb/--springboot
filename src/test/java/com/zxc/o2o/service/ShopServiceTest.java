package com.zxc.o2o.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/9 21:55
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void getByShopId() {
        System.out.println(shopService.getByShopId(1));
    }
}