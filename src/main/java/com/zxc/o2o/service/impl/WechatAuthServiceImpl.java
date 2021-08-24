package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.PersonInfoDao;
import com.zxc.o2o.dao.WechatAuthDao;
import com.zxc.o2o.dto.WechatAuthExecution;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.enums.WechatAuthStateEnum;
import com.zxc.o2o.exceptions.WechatAuthOperationException;
import com.zxc.o2o.service.WechatAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 7:31
 * @Version 1.0
 *
 */
@Slf4j
@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WechatAuth findWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
        //空值判断
        if (wechatAuth == null || wechatAuth.getOpenId() == null){
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            //设置创建时间
            wechatAuth.setCreateTime(new Date());
            //如果微信账号里夹带这用户信息，并且用户Id为空，则认为该用户第一次使用平台，并且通过微信登录
            //则自动创建用户信息
            if (wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null){
                try {
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setLastEditTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = wechatAuth.getPersonInfo();
                    int result = personInfoDao.insertPersonInfo(personInfo);
                    if (result < 0){
                        throw new WechatAuthOperationException("添加用户信息失败");
                    }
                }catch (Exception e){
                    log.error("insertPersonInfo error:"+e.toString());
                    throw new WechatAuthOperationException("insertPersonInfo error:"+e.getMessage());
                }
            }

            //创建平台专属的微信账号
            int result = wechatAuthDao.insertWechatAuth(wechatAuth);
            if (result < 0){
                throw new WechatAuthOperationException("账号创建失败");
            }else {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
            }

        }catch (Exception e){
            log.error("insertWechatAuth error:"+e.toString());
            throw new WechatAuthOperationException("insertWechatAuth error:"+e.getMessage());
        }
    }
}
