package com.zxc.o2o.service;

import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.dto.ProductExecution;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {

    /*
    * product
    * thumbnail
    * productImgs
    * */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,
                                List<ImageHolder> productImgList)throws ProductOperationException;

    /*
     * 根据ShopCondition返回响应店铺列表
     * */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    //通过商品id查询唯一的商品信息
    Product getProductById(long productId);

    //修改商品信息以及图片处理
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException;
}
