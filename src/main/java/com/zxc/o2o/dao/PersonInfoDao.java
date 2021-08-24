package com.zxc.o2o.dao;

import com.zxc.o2o.entity.PersonInfo;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/2 22:01
 * @Version 1.0
 *
 */
public interface PersonInfoDao {
    /*
    * 通过id查询用户
    * */
    PersonInfo queryPersonInfoById(long userId);

    /*
    * 添加用户信息
    * */
    int insertPersonInfo(PersonInfo personInfo);
}
