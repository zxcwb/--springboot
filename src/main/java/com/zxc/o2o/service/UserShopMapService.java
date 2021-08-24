package com.zxc.o2o.service;

import com.zxc.o2o.dto.UserProductMapExecution;
import com.zxc.o2o.dto.UserShopMapExecution;
import com.zxc.o2o.entity.UserProductMap;
import com.zxc.o2o.entity.UserShopMap;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 16:57
 * @Version 1.0
 *
 */
public interface UserShopMapService {
    /*
    * 根据传入的查询信息分页查询用户积分列表
    * */
    UserShopMapExecution listUserShopMap(UserShopMap userShopCondition,
                                            Integer pageIndex, Integer pageSize);

    /*
    * 根据用户Id和店铺Id返回该用户在某个店铺的积分情况
    * */
    UserShopMap getUserShopMap(long userId,long shopId);
}
