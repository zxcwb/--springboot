package com.zxc.o2o.dao;

import com.zxc.o2o.entity.Award;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 8:58
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {

    @Autowired
    private AwardDao awardDao;

    @Test
    public void queryAwardList() {
    }

    @Test
    public void queryAwardCount() {
    }

    @Test
    public void queryAwardByAwardById() {
       Award award =  awardDao.queryAwardByAwardById(1);
       System.out.println(award);
    }

    @Test
    public void insertAward() {
    }

    @Test
    public void updateAward() {
    }

    @Test
    public void deleteAward() {
    }
}