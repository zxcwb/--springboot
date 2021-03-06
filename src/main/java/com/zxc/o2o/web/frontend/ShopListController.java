package com.zxc.o2o.web.frontend;

import com.zxc.o2o.dto.ShopExecution;
import com.zxc.o2o.entity.Area;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.ShopCategory;
import com.zxc.o2o.service.AreaService;
import com.zxc.o2o.service.ShopCategoryService;
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
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private ShopService shopService;

    //返回商店列表里的ShopCategory列表（二级或者一级），一级区域信息列表
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //试着从前端请求中获取parentId
        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
       /* //为了shopdetail的返回按钮
        request.getSession().setAttribute("parentId",parentId);*/
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1){
            //如果parentId存在，则取出一级ShopCategory下的二级ShopCategory列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else {
            try {
                //如果不存在parentId，则取出所有一级ShopCategory（就是用户点击全部商店列表）,传入null就是查询出所有的商店类型列表
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try {
            //获取区域信息列表
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    //获取指定查询条件下的店铺列表
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取一页需要显示的条数
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //非空判断
        if ((pageIndex > -1) && (pageSize > -1)){
            //试着获取一级类别Id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
           /* //为了shopdetail的返回按钮
            request.getSession().setAttribute("parentId",parentId);*/
            //试着获取特定的二级类别Id
            long shopCategoryId = HttpServletRequestUtil.getLong(request,
                    "shopCategoryId");
            //试着获取区域Id
            long areaId = HttpServletRequestUtil.getLong(request, "areaId");
            //试着获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request,
                    "shopName");
            //拼接处组合之后的查询条件
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            System.out.println(shopCondition);
            //根据查询条件和分页信息获取店铺列表，并返回总数
            ShopExecution shopExecution = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("count",shopExecution.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex");
        }
        return modelMap;
    }

    //组合查询条件，并将条件封装到ShopCondition对象里返回
    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, long areaId, String shopName) {
         Shop shopCondition = new Shop();
         if (parentId != -1L){
             //查询某个大类下面的小类下的所有店铺列表
             ShopCategory childCategory = new ShopCategory();
             ShopCategory parentCategory = new ShopCategory();
             parentCategory.setShopCategoryId(parentId);
             childCategory.setParent(parentCategory);
             shopCondition.setShopCategory(childCategory);
         }
         if (shopCategoryId != -1L){
             //查询某个二级(小类)ShopCategory下面的店铺列表
             ShopCategory shopCategory = new ShopCategory();
             shopCategory.setShopCategoryId(shopCategoryId);

             shopCondition.setShopCategory(shopCategory);
         }
         if (areaId != -1L){
             //查询位于某个区域的Id下的店铺列表
             Area area = new Area();
             //这里的实体类的areaId 为Integer类型
             area.setAreaId((int) areaId);

             shopCondition.setArea(area);
        }
         if (shopName != null){
             //查询名字里包含shopName的店铺列表
             shopCondition.setShopName(shopName);

         }
         //前端展示店铺的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
         return shopCondition;
    }
}
