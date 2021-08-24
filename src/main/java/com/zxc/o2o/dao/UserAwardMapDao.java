package com.zxc.o2o.dao;

import com.zxc.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 10:04
 * @Version 1.0
 *
 */
public interface UserAwardMapDao {
    /*
    * 根据传入进来的查询条件分页返回用户兑换奖品记录的列表信息
    * */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition")UserAwardMap userAwardMapCondition,
                                             @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    /*
    * 配合queryUserListAwardMapList返回相同条件下的兑换奖品记录数
    * */
    int queryUserAwardMapCount(@Param("userAwardCondition")UserAwardMap userAwardMapCondition);

    /*
    * 根据userAwardId返回某条奖品兑换信息
    * */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /*
    * 添加一条奖品兑换信息
    * */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /*
    * 更新一条奖品兑换信息
    * */
    int updateUserAwardMap(UserAwardMap userAwardMap);
}
