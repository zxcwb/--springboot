package com.zxc.o2o.service;

import com.zxc.o2o.dto.UserAwardMapExecution;
import com.zxc.o2o.entity.UserAwardMap;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 17:21
 * @Version 1.0
 *
 */
public interface UserAwardMapService {
    /*
    * 根据传入的查询条件分页获取映射列表以及总数
    * */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize);

    /*
     *领取奖品，添加映射信息
     * */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMapCondition);

    /*
    * 根据传入的Id获取映射信息
    * */
    UserAwardMap getUserAwardMapById(long userAwardId);



    /*
    * 修改映射信息
    * */
    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMapCondition);
}
