package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.ProductCategoryDao;
import com.zxc.o2o.dao.ProductDao;
import com.zxc.o2o.dto.ProductCategoryExecution;
import com.zxc.o2o.entity.ProductCategory;
import com.zxc.o2o.enums.ProductCategoryStateEnum;
import com.zxc.o2o.exceptions.ProductCategoryOperationException;
import com.zxc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    public List<ProductCategory> getProductCategoryListToAddProduct() {
        return productCategoryDao.queryProductCategoryListToAddProduct();
    }


    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategoryList(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList != null && productCategoryList.size()>0){
            try {
                int nums = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (nums <= 0){
                    throw new ProductCategoryOperationException("店铺类别创建失败！");
                }else {
                    System.out.println("添加商品类别成功"+nums);
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch (Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory err:"+e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        //TODO 将此类别下的商品的类别Id（ProductCategoryId）置空
        try {
            int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectedNum < 0){
                throw new RuntimeException("商品类别更新失败！");
            }
        }catch (Exception e){
            throw new RuntimeException("deleteProductCategory error:"+e.getMessage());
        }
        try {
            int nums = productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if (nums <= 0){
                throw  new ProductCategoryOperationException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch (ProductCategoryOperationException e){
            throw new ProductCategoryOperationException("deleteProductCategory error："+e.getMessage());
        }
    }

}
