package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.UserShopMapDao;
import com.zxc.o2o.dto.UserShopMapExecution;
import com.zxc.o2o.entity.UserProductMap;
import com.zxc.o2o.entity.UserShopMap;
import com.zxc.o2o.service.UserShopMapService;
import com.zxc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 21:39
 * @Version 1.0
 *
 */
@Service
public class UserShopMapServiceImpl implements UserShopMapService {

    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserShopMapExecution listUserShopMap(UserShopMap userShopCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userShopCondition != null && pageIndex != null && pageSize != null){
            //页换行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            //调用dao层取数据
            List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopCondition,beginIndex,pageSize);
            int count = userShopMapDao.queryUserShopMapCount(userShopCondition);
            UserShopMapExecution ue = new UserShopMapExecution();
            ue.setUserShopMapList(userShopMapList);
            ue.setCount(count);
            return ue;
        }else {
            return null;
        }
    }

    @Override
    public UserShopMap getUserShopMap(long userId, long shopId) {
        return userShopMapDao.queryUserShopMap(userId,shopId);
    }
}
