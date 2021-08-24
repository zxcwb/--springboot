package com.zxc.o2o.service;

import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.dto.ShopExecution;
import com.zxc.o2o.entity.Shop;

public interface ShopService {

    /*
    * 根据ShopCondition返回响应店铺列表
    * */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    //根据shopId获取店铺信息
    Shop getByShopId(long shopId);

    //修改店铺信息
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail);

    //ShopExecution addShop(Shop shop,File shopImg);
    ShopExecution addShop(Shop shop, ImageHolder thumbnail);
}
