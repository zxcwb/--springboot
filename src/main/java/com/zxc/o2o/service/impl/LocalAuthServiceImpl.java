package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.LocalAuthDao;
import com.zxc.o2o.dao.PersonInfoDao;
import com.zxc.o2o.dto.LocalAuthExecution;
import com.zxc.o2o.entity.LocalAuth;
import com.zxc.o2o.enums.LocalAuthStateEnum;
import com.zxc.o2o.exceptions.LocalAuthOperationException;
import com.zxc.o2o.service.LocalAuthService;
import com.zxc.o2o.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 11:36
 * @Version 1.0
 *
 */
@Slf4j
@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public LocalAuth getLocalAuthByUsernameAndPassword(String username, String password) {
        LocalAuth localAuth = localAuthDao.queryLocalByUsernameAndPassword(username, MD5.getMd5(password));
        if (localAuth != null){
            if (localAuth.getUserType() == null){
                localAuth.setUserType(personInfoDao.queryPersonInfoById(localAuth.getUserId()).getUserType());
            }
        }
        return localAuth;
    }

    @Override
    public LocalAuth getLocalAuthByUserId(Long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        //空值判断，传入的localAuth账号密码，用户信息，特别是userId不能为空，否则直接返回错误
        if (localAuth == null || localAuth.getPassword() == null || localAuth.getUserName() == null
        ||localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }

        //查询此用户已经绑定过平台账号
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if (tempAuth != null){
            //如果绑定过则直接退出，以保证平台账号的唯一性
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }

        try {
            //如果之前没有绑定过平台账号，则创建一个平台账号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            //对密码进行加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            /*//如果是第一次注册或者绑定
            if (tempAuth.getPersonInfo().getUserType() != null){
                localAuth.setUserType(tempAuth.getPersonInfo().getUserType());
            }*/
            int result = localAuthDao.insertLocalAuth(localAuth);
            if (result <= 0){
                throw new LocalAuthOperationException("账号绑定失败！");
            }else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
            }
        }catch (Exception e){
            throw new LocalAuthOperationException("insertLocalAuth error:"+e.getMessage());
        }
    }

    @Override
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) {
        //非空判断，判断传入的userId，账号，密码是否为null,新旧密码是否相同，若不满足条件，则返回错误信息
        if (userId != null && username != null && password != null && newPassword != null && !password.equals(newPassword)){
            try {
                //更新密码，并对新密码进行md5加密
                int result = localAuthDao.updatePasswordByUserIdUserNameAndPassword(userId,username,MD5.getMd5(password),MD5.getMd5(newPassword),new Date());
                if (result <= 0){
                    throw new LocalAuthOperationException("更新密码失败！");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new LocalAuthOperationException("更新密码失败！"+e.toString());
            }
        }else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }


}
