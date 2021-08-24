package com.zxc.o2o.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 11:15
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAwardMapDaoTest {

    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Test
    public void queryUserAwardMapById() {
        System.out.println(userAwardMapDao.queryUserAwardMapById(1));
    }
}