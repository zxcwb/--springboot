package com.zxc.o2o.service;

import com.zxc.o2o.dto.UserProductMapExecution;
import com.zxc.o2o.entity.UserProductMap;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 22:10
 * @Version 1.0
 *
 */
public interface UserProductMapService {
    /*
    * 通过传入的查询条件分页列出用户消费信息列表
    * */
    UserProductMapExecution listUserProductMap(UserProductMap userProductCondition,
                                               Integer pageIndex, Integer pageSize);

    /*
    * 添加消费记录
    * */
    UserProductMapExecution addUserProductMap(UserProductMap userProductMap);
}
