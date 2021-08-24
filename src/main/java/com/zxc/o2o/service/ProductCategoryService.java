package com.zxc.o2o.service;

import com.zxc.o2o.dto.ProductCategoryExecution;
import com.zxc.o2o.entity.ProductCategory;
import com.zxc.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(long shopId);

    List<ProductCategory> getProductCategoryListToAddProduct();

    ProductCategoryExecution batchAddProductCategoryList(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)throws ProductCategoryOperationException;
}
