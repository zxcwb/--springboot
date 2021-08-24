package com.zxc.o2o.dao;

import com.zxc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    //查询商品列表并分页，可输入条件：商品名（模糊）、商品状态、店铺Id、商品类别
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int queryProductCount(@Param("productCondition") Product productCondition);

    //根据商品Id查询商品   ！！！！！！！！！！！！！！！！！！！！！！！未做test！！！！！！！！！！！！！！！！！！！
    Product queryProductByProductId(Long productId);

    //插入商品
    int insertProduct(Product product);

    //更新商品信息
    int updateProduct(Product product);

    /**
     * 删除商品类别之前，将商品类别ID置为空
     */
    int updateProductCategoryToNull(long productCategoryId);

    //删除商品信息
    int deleteProduct(Long productId);

}
