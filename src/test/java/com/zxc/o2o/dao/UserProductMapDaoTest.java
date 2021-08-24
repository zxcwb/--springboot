package com.zxc.o2o.dao;

import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.UserProductMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 20:14
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProductMapDaoTest {

    @Autowired
    private UserProductMapDao userProductMapDao;

    @Test
    public void queryUserProductMapList() {
    }

    @Test
    public void queryUserProductMapCount() {
    }

    @Test
    public void insertUserProductMap() {
        UserProductMap userProductMap = new UserProductMap();
        userProductMap.setCreateTime(new Date());
        userProductMap.setOperator(new PersonInfo());
        userProductMap.setPoint(1);
        userProductMap.setProduct(new Product());
        userProductMap.setUser(new PersonInfo());
        userProductMap.setShop(new Shop());
        int result = userProductMapDao.insertUserProductMap(userProductMap);
        System.out.println(result);
    }
}