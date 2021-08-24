package com.zxc.o2o.web.frontend;

import com.zxc.o2o.dto.ProductExecution;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.entity.ProductCategory;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.service.ProductCategoryService;
import com.zxc.o2o.service.ProductService;
import com.zxc.o2o.service.ShopService;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    //获取店铺信息以及该店下面的商品类别列表
    @RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取前台传过来的shopId
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if (shopId != -1){
            //获取店铺Id为shopId的店铺信息
            shop = shopService.getByShopId(shopId);
            //获取店铺下面的商品类别列表
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    //依据查询条件分页列出该店铺的下面的所有商品
    @RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取分页页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取一页需要显示的条数
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //获取店铺Id
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        //空值判断
        if ((pageIndex > -1 && pageSize > -1 && shopId > -1)){
            //尝试获取商品类别Id
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            //尝试获取模糊查找的商品名
            String productName = HttpServletRequestUtil.getString(request,"productName");
            //组合查询条件
            Product productCondition = compactProductCondition4Search(shopId,productCategoryId,productName);
            //按照传入的查询条件以及分页信息返回相应的商品列表以及总数
            ProductExecution productExecution = productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("productList",productExecution.getProductList());
            modelMap.put("count",productExecution.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product product = new Product();
            Shop shop = new Shop();
            shop.setShopId(shopId);
            product.setShop(shop);
        if (productCategoryId > -1){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }
        if (productName != null){
            product.setProductName(productName);
        }
        product.setEnableStatus(1);
        return product;
    }
}
