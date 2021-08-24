package com.zxc.o2o.dao;

import com.zxc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    //通过shop id 查询店铺商品类别
    List<ProductCategory> queryProductCategoryList(long shopId);

    List<ProductCategory> queryProductCategoryListToAddProduct();

    //批量新增商品类别
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    //批量删除商品类别
    int deleteProductCategory(@Param("productCategoryId") long productCategoryId
            , @Param("shopId") long shopId);
}
