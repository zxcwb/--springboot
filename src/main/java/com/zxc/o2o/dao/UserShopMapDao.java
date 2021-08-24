package com.zxc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zxc.o2o.entity.UserShopMap;

public interface UserShopMapDao {
	/**
	 * 根据查询条件返回用户店铺积分
	 * @param userShopCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserShopMap> queryUserShopMapList(
            @Param("userShopCondition") UserShopMap userShopCondition,
            @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/*
	* 根据用户传入的id和shopId查询该用户在这个店铺下的积分
	* */
	UserShopMap queryUserShopMap(@Param("userId") long userId,
                                 @Param("shopId") long shopId);

	/**
	 * 根据相同的查询条件返回用户店铺积分记录总数
	 * @param userShopCondition
	 * @return
	 */
	int queryUserShopMapCount(
            @Param("userShopCondition") UserShopMap userShopCondition);

	/**
	 * 
	 * @param userShopMap
	 * @return
	 */
	int insertUserShopMap(UserShopMap userShopMap);

	/**
	 * 
	 * @param userShopMap
	 * @return
	 */
	int updateUserShopMapPoint(UserShopMap userShopMap);

}
