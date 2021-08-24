package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.ShopDao;
import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.dto.ShopExecution;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.enums.ShopSateEnum;
import com.zxc.o2o.exceptions.ShopOperationException;
import com.zxc.o2o.service.ShopService;
import com.zxc.o2o.util.FileUtil;
import com.zxc.o2o.util.ImageUtil;
import com.zxc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("shopService")
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //将页码转换为行码
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        //依据查询条件，调用dao层返回相关的店铺列表
        List<Shop> shopList = shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        //依据相同的查询条件，返回店铺总数
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null){
            se.setShopList(shopList);
            se.setCount(count);
        }else {
            se.setState(ShopSateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    //更新店铺信息
    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) {
        if (shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopSateEnum.NULL_SHOP);
        }else {

           try {
               //1、判断是否需要处理图片，更新店铺信息
               if (thumbnail != null){
                   if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())){
                       Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                       if (tempShop.getShopImg() != null){
                           ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                       }
                       addShopImg(shop,thumbnail);
                   }
               }
               //2、更新店铺信息
               shop.setLastEditTime(new Date());
               int effectedNum = shopDao.updateShop(shop);
               if (effectedNum <= 0){
                   return new ShopExecution(ShopSateEnum.INNER_ERROR);
               }else {
                   shop = shopDao.queryByShopId(shop.getShopId());
                   return new ShopExecution(ShopSateEnum.SUCCESS,shop);
               }
           }catch (Exception e){
               throw new ShopOperationException("modifyShop error："+e.getMessage());
           }
        }
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) { //此处shopImg原本是File类型
        //空值判断
        if (shop == null){
            return new ShopExecution(ShopSateEnum.NULL_SHOP);
        }
        try {
            //申请成功，给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            //为了防止异常
            shop.setShopImg("");
            shop.setPriority(1);
            //添加店铺信息
            int nums = shopDao.insertShop(shop);
            if (nums <= 0){
                throw new ShopOperationException("店铺创建失败");
            }else {
                if (thumbnail.getImage() != null){
                    //存储图片
                    try {
                        addShopImg(shop,thumbnail);
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg Error："+e.getMessage());
                    }
                    //更新店铺的图片地址
                    nums = shopDao.updateShop(shop);
                    if (nums <= 0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }


        }catch (Exception e){
            throw new ShopOperationException("addShop error:"+e.getMessage());
        }
        return new ShopExecution(ShopSateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop, ImageHolder thumbnail) { //此处shopImg原本是File类型
        //获取shop图片相对值路径
        String dest = FileUtil.getShopImagePath(shop.getShopId());
        //FileItem fileItem = FileToCommonsMultipartFileUtil.inputStreamToFile(shopImg,"shopImg");
        //将File类型转化为CommonsMultipartFile
        //CommonsMultipartFile shopImgs = new CommonsMultipartFile(fileItem);
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        System.out.println("shopImgAddr=============================="+shopImgAddr);
        shop.setShopImg(shopImgAddr);
    }

}
