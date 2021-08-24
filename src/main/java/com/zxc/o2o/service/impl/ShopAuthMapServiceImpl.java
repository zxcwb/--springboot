package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.ShopAuthMapDao;
import com.zxc.o2o.dto.ShopAuthMapExecution;
import com.zxc.o2o.entity.ShopAuthMap;
import com.zxc.o2o.enums.ShopAuthMapStateEnum;
import com.zxc.o2o.exceptions.ShopAuthMapOperationException;
import com.zxc.o2o.service.ShopAuthMapService;
import com.zxc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 22:47
 * @Version 1.0
 *
 */
@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (shopId != null && pageIndex != null && pageSize != null){
            //页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            //查询结果返回店铺授权信息列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId,pageIndex,pageSize);
            //返回总数
            int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
            ShopAuthMapExecution se = new ShopAuthMapExecution();
            se.setShopAuthMapList(shopAuthMapList);
            se.setCount(count);
            return se;
        }else {
            return null;
        }
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }

    @Override
    @Transactional
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws RuntimeException {
        //空值判断
        if (shopAuthMap != null && shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null
                &&shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null){
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setTitleFlag(0);
            try {
                //添加授权信息
                int result = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (result <= 0){
                    throw new ShopAuthMapOperationException("添加授权失败");
                }
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,shopAuthMap);
            }catch (Exception e){
                throw new ShopAuthMapOperationException("添加授权失败："+e.toString());
            }
        }else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
        }
    }

    @Override
    @Transactional
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws RuntimeException {
        //空值判断
        if (shopAuthMap == null && shopAuthMap.getShopAuthId() == null){
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
        }else {
            try {
                int result = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if(result <= 0){
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
                }else {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,shopAuthMap);
                }
            }catch (Exception e){
                throw new ShopAuthMapOperationException("modifyShopAuthMap error:"+e.toString());
            }
        }
    }

    @Override
    public ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId) throws RuntimeException {
        return null;
    }


}
