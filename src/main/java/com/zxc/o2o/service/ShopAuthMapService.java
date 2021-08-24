package com.zxc.o2o.service;

import com.zxc.o2o.dto.ShopAuthMapExecution;
import com.zxc.o2o.entity.ShopAuthMap;

public interface ShopAuthMapService {
	/**
	 * 
	 * @param shopId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * 根据店铺Id分页显示该店铺的授权信息
	 */
	ShopAuthMapExecution listShopAuthMapByShopId(Long shopId,
                                                 Integer pageIndex, Integer pageSize);

	/**
	 * 添加授权信息
	 * @param shopAuthMap
	 * @return
	 * @throws RuntimeException
	 */
	ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap)
			throws RuntimeException;

	/**
	 * 更新授权信息，包括职位等
	 *
	 * @return
	 * @throws RuntimeException
	 */
	ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws RuntimeException;

	/**
	 * 移除根据Id主键
	 * @param shopAuthMapId
	 * @return
	 * @throws RuntimeException
	 */
	ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId)
			throws RuntimeException;

	/**
	 * 根据shopAuthId返回对应的授权信息
	 * @param shopAuthId
	 * @return
	 */
	ShopAuthMap getShopAuthMapById(Long shopAuthId);

}
