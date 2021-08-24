package com.zxc.o2o.service;

import com.zxc.o2o.dto.WechatAuthExecution;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/8 15:59
 * @Version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {

    @Autowired
    private WechatAuthService  wechatAuthService;

    @Test
    public void findWechatAuthByOpenId() {
    }

    @Test
    public void register() {
        //新增一条微信号
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        String openId = "dafasdsadas";
        //给微信号设置上用户信息，但不设置用户Id，创建微信账号的时候自动创建用户信息
        personInfo.setCreateTime(new Date());
        personInfo.setName("测试");
        personInfo.setUserType(1);
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId(openId);
        wechatAuth.setCreateTime(new Date());
        WechatAuthExecution wechatAuthExecution = wechatAuthService.register(wechatAuth);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(),wechatAuthExecution.getState());
        //通过openId找到新增的wechatAuth
        wechatAuth = wechatAuthService.findWechatAuthByOpenId(openId);
        //打印出用户名字看看跟预期是否相符
        System.out.println(wechatAuth.getPersonInfo().getName());
    }
}