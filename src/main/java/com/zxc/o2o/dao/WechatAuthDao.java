package com.zxc.o2o.dao;

import com.zxc.o2o.entity.WechatAuth;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/2 22:04
 * @Version 1.0
 *
 */
public interface WechatAuthDao {

    /*
    * 通过openId查询对应本平台的微信账号
    * */
    WechatAuth queryWechatInfoByOpenId(String openId);

    /*
    * 添加对应本平台的微信账号
    * */
    int insertWechatAuth(WechatAuth wechatAuth);
}
