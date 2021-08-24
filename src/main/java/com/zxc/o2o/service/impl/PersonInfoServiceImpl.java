package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.PersonInfoDao;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 7:48
 * @Version 1.0
 *
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}
