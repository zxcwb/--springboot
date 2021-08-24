package com.zxc.o2o.dao;

import com.zxc.o2o.dto.UserShopMapExecution;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.UserShopMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 22:20
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserShopMapDaoTest {

    @Autowired
    private UserShopMapDao userShopMapDao;

    @Test
    public void queryUserShopMapList() {
        Shop shop = new Shop();
        shop.setShopId(18L);
        PersonInfo user = new PersonInfo();
        user.setUserId(18L);
        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setShop(shop);
        userShopMap.setUser(user);
        List<UserShopMap> userShopMapList =  userShopMapDao.queryUserShopMapList(userShopMap,1,99);
        System.out.println(userShopMapList);
    }

    @Test
    public void queryUserShopMap() {
    }

    @Test
    public void queryUserShopMapCount() {
    }

    @Test
    public void insertUserShopMap() {
        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setCreateTime(new Date());
        userShopMap.setPoint(1);
        userShopMap.setUserShopId(18L);
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(18L);
        userShopMap.setUser(personInfo);
        Shop shop = new Shop();
        shop.setShopId(18L);
        userShopMap.setShop(shop);
        assertEquals(1,userShopMapDao.insertUserShopMap(userShopMap));
    }

    @Test
    public void updateUserShopMapPoint() {
    }

    @Test
    public void testQueryUserShopMapList() {
    }
}