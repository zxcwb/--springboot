package com.zxc.o2o.service;

import com.zxc.o2o.dto.LocalAuthExecution;
import com.zxc.o2o.entity.LocalAuth;
import com.zxc.o2o.exceptions.LocalAuthOperationException;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 11:24
 * @Version 1.0
 *
 */
public interface LocalAuthService {
    /*
    通过账号和密码获取平台账号信息
    * */
    LocalAuth getLocalAuthByUsernameAndPassword(String username, String password);

    /*
    * 通过userId获取平台账号信息
    * */
    LocalAuth getLocalAuthByUserId(Long userId);

    /*
    * 绑定微信，生成平台专属账号
    * */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth)throws LocalAuthOperationException;

    /*
    * 修改平台账号的登录密码
    * */
    LocalAuthExecution modifyLocalAuth(Long userId, String username,
                                       String password, String newPassword);
}
