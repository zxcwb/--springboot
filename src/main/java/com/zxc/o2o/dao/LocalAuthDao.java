package com.zxc.o2o.dao;

import com.zxc.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 10:52
 * @Version 1.0
 *  本地账号Dao
 */
public interface LocalAuthDao {

    /*通过账号和面验证信息，登录用
    * */
    LocalAuth queryLocalByUsernameAndPassword(@Param("username") String username,
                                              @Param("password") String password);

    /*
    * 通过用户Id查询对应的localAuth
    * */
    LocalAuth queryLocalByUserId(@Param("userId") Long userId);

    /*
    * 添加平台账号
    * */
    int insertLocalAuth(LocalAuth localAuth);

    /*
    * 通过userId，username，password更改密码
    * */
    int updatePasswordByUserIdUserNameAndPassword(@Param("userId") Long userId, @Param("username") String username,
                                                  @Param("password") String password, @Param("newPassword") String newPassword,
                                                  @Param("lastEditTime") Date lastEditTime);
}
