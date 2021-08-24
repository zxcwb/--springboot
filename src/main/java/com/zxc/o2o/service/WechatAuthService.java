package com.zxc.o2o.service;

import com.zxc.o2o.dto.WechatAuthExecution;
import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.exceptions.WechatAuthOperationException;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 7:08
 * @Version 1.0
 *
 */
public interface WechatAuthService {
    /*
    * 通过openId查找平台对应的微信账号
    * */
    WechatAuth findWechatAuthByOpenId(String openId);

    /*
    * 注册本平台的微信账号
    * 设置成继承RuntimeException是为了让其支持事务的回滚
    *
    * Spring声明式事务管理默认对非检查型异常和运行时异常进行事务回滚，而对检查型异常则不进行回滚操作。
    *
    * 什么是检查型异常和非检查型异常？
    *
    * 最简单的判断点有两个：
    * 1、继承自runtimeException或error的是非检查型异常，而继承自exception的是检查型异常。
    * 2、对非检查型异常可以不用捕获，而检查型异常必须用try语句块进行处理或者把异常交给上级方法处理，总之就是必须写代码处理它。所以必须service捕获异常，然后再次抛出，这样事务才能生效。
    *
    * */
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
