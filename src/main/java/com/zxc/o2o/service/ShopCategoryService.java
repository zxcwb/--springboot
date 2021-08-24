package com.zxc.o2o.service;

import com.zxc.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    public static final String SCLISTKEY = "shopcategorylist";
    //根据传入的条件返回指定的商品类型列表
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
