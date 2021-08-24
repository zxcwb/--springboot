package com.zxc.o2o.service;

import com.zxc.o2o.dto.WechatAuthExecution;
import com.zxc.o2o.entity.Area;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.enums.WechatAuthStateEnum;
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
 * @Date: 2021/8/8 15:58
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {

    @Autowired
    private AreaService areaService;

    @Autowired
    private CacheService cacheService;

    @Test
    public void getAreaList() {
        List<Area> areaList = areaService.getAreaList();
        assertEquals("西苑",areaList.get(0).getAreaName());
        cacheService.removeFormCache(areaService.AREALISTKEY);
        areaList = areaService.getAreaList();
    }
}