package com.zxc.o2o.service;

import com.zxc.o2o.entity.PersonInfo;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 7:47
 * @Version 1.0
 *
 */
public interface PersonInfoService {
    /*
    * 通过用户Id获取personInfo信息
    * */
    PersonInfo getPersonInfoById(Long userId);
}
