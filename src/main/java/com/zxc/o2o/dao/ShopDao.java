package com.zxc.o2o.dao;

import com.zxc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {

    /*
    * 分页查询店铺，可输入的条件有：店铺名（模糊）、店铺状态、店铺类别、区域Id，owner
    * rowIndex：从第几行开始取，返回的行数是多少条
    * pageSize：返回的条数
    * */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    Shop queryByShopId(long ShopId);

    //新增店铺
    int insertShop(Shop shop);

    //更新店铺
    int updateShop(Shop shop);

}
