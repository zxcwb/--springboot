package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.ProductDao;
import com.zxc.o2o.dao.ProductImgDao;
import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.dto.ProductExecution;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.entity.ProductImg;
import com.zxc.o2o.enums.ProductStateEnum;
import com.zxc.o2o.exceptions.ProductOperationException;
import com.zxc.o2o.service.ProductService;
import com.zxc.o2o.util.FileUtil;
import com.zxc.o2o.util.ImageUtil;
import com.zxc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;


    @Override
    @Transactional
    /*
    * 1、处理缩略图，获取缩略图相对路径并赋值给product
    * 2、往tb_product写入商品信息，获取productId
    * 3、结合productId批量处理商品详情图
    * 4、将商品详情图列表批量插入tb_product_img中   ！！！！！！！！！！！！！！！！！！！！未做test！！！！！！！！！！！！！！！！！！！！！
    * */
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null &&
                product.getShop().getShopId() != null){
            //给商品设置上默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认为上架状态
            product.setEnableStatus(1);
            //若商品缩略图不为空则添加
            if (thumbnail != null){
                addThumbnail(product,thumbnail);
            }
            try {
                //创建商品信息
                int nums = productDao.insertProduct(product);
                if (nums <= 0){
                    throw new ProductOperationException("创建商品失败！");
                }
            }catch (Exception e){
                throw  new ProductOperationException("创建商品失败："+e.toString());
            }
            if (productImgList != null && productImgList.size() > 0){
                addProductImgList(product,productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS,product);
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(productCondition,rowIndex,pageSize);
        int count = productDao.queryProductCount(productCondition);
        ProductExecution se = new ProductExecution();
        if (productList != null){
            se.setProductList(productList);
            se.setCount(count);
        }else {
            se.setState(ProductStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    /*
    * 1、如果缩略图参数有值，则处理缩略图
    * 2、若原先存在缩略图，需要先删除再添加新图，之后获取缩略图相对路径并赋值给product
    * 3、如果商品详情图列表有值，先删除在进行添加，之后获取相对路径赋值给productImg
    * 4、将tb_product_img下面的该商品原先商品的详情图全部清除
    * 5、更新tb_product，
    * */
    @Transactional
    @Override
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //给商品设置上默认属性
            product.setLastEditTime(new Date());
            //若商品缩略图不为空则删除原有的缩略图并添加
            if (thumbnail != null){
                //先获取一遍原有的信息，因为原有的信息里有原图片地址
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                if (tempProduct.getImgAddr() != null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product,thumbnail);
            }
            //如果有新存入的商品详情图，那么则先删除原有的图片，然后在添加新的图片
            if (productImgHolderList != null && productImgHolderList.size() > 0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,productImgHolderList);
            }
            try {
                //更新商品信息
                System.out.println(product);
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0){
                    throw  new ProductOperationException("更新商品信息失败！");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException("更新商品信息失败！"+e.toString());
            }
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    //删除某个商品下的所有详情图
    private void deleteProductImgList(Long productId) {
        //根据productId获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //删除掉原有的所有的商品详情图
        for (ProductImg productImg : productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除掉数据库中所有的图片的信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    //批量添加图片
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList){
        //获取图片存储路径，这里直接存放到相应的店铺的文件夹下
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        //遍历图片一次去处理，并添加进productImg实体类里
        for (ImageHolder productImgHodler : productImgHolderList){
            String imgAddr = ImageUtil.generateNormalImg(productImgHodler,dest);
            ProductImg productImg = new ProductImg();
            //商品详情图地址
            //System.out.println("+++++++++++++++++++++++++++++"+imgAddr);
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果有图片需要添加，就执行批量添加操作
        if (productImgList.size() > 0){
            try {
                int nums = productImgDao.batchInsertProductImg(productImgList);
                if (nums <= 0){
                    throw new ProductOperationException("创建商品详情图失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品详情图失败"+e.toString());
            }
        }
    }

    private void addThumbnail(Product product, ImageHolder thumbnail) {
        //获取商品图片路径
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        System.out.println("thumbnail=====>"+thumbnailAddr+"========================zxc");
        product.setImgAddr(thumbnailAddr);
    }
}
