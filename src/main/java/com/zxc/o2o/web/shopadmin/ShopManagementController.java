package com.zxc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.dto.ShopExecution;
import com.zxc.o2o.entity.Area;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.ShopCategory;
import com.zxc.o2o.enums.ShopSateEnum;
import com.zxc.o2o.exceptions.ShopOperationException;
import com.zxc.o2o.service.AreaService;
import com.zxc.o2o.service.ShopCategoryService;
import com.zxc.o2o.service.ShopService;
import com.zxc.o2o.util.CodeUtil;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/getshopmanagerinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object>  getShopManagerInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId <= 0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //暂时设置
        PersonInfo user = new PersonInfo();
        /*  user.setUserId(8L);
        user.setName("test");
        request.getSession().getAttribute("user");*/
        user = (PersonInfo) request.getSession().getAttribute("user");
        System.out.println("user==========="+user);
        List<Shop> shopList = new ArrayList<Shop>();
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition,0,100);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopById(HttpServletRequest request){
        Map<String ,Object> modelMap = new HashMap<String,Object>();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        System.out.println("shopId=========="+shopId);
        if (shopId > -1){
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<>();
        try {
            //获取parent_id不为空的店铺类别列表，如果redis出了问题也会无法获取Could not get a resource from the pool
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            //获取区域列表
            areaList = areaService.getAreaList();
            //registry.js 26
            Shop shop = new Shop();
            modelMap.put("shop",shop);
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> registerShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //1、接收并转化相应的参数，包括店铺信息和图片信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            System.out.println(modelMap.get("success"));
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;  //此处将CommonsMultipartFile --> File
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //shopImg = (File) multipartHttpServletRequest.getFile("shopImg");
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            System.out.println(modelMap.get("success"));
            return modelMap;
        }
        //2、注册店铺
        if (shop != null && shopImg != null){
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            //session的添加
            //暂时没有owner
            shop.setOwnerId(owner.getUserId());
            shop.setOwner(owner);
            ShopExecution se = null;//如果此处抛出异常，那么就是转化文件类型的原因，这里错了addShopImg
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                se = shopService.addShop(shop, imageHolder);

                if (se.getState() == ShopSateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    //该用户可以操作的用户列表
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<Shop>();
                    }
                        shopList.add(se.getShop());
                        request.getSession().setAttribute("shopList",shopList);

                    return modelMap;
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                    System.out.println(modelMap.get("success"));
                    return modelMap;
                }
            }catch (ShopOperationException e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }catch(IOException e){
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.getMessage());
             }
            return modelMap;

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            System.out.println(modelMap.get("success"));
            return modelMap;
        }
        //3、返回结果
    }

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //1、接收并转化相应的参数，包括店铺信息和图片信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        //设置shopId
        try {
            shop = mapper.readValue(shopStr, Shop.class);
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwnerId(owner.getUserId());
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            System.out.println(modelMap.get("success"));
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;  //此处将CommonsMultipartFile --> File
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //shopImg = (File) multipartHttpServletRequest.getFile("shopImg");
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //可以不上传图片
        /*else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            System.out.println(modelMap.get("success"));
            return modelMap;
        }*/
        //2、修该店铺信息
        /*long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        System.out.println("shopId="+shopId+"======================");
        shop.setShopId(shopId);*/

        if (shop != null && shop.getShopId() != null){
            ShopExecution se = null;//如果此处抛出异常，那么就是转化文件类型的原因
            try {
                if (shopImg == null){
                    se = shopService.modifyShop(shop,null);
                }else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    se = shopService.modifyShop(shop,imageHolder);
                }
            }catch (ShopOperationException e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }catch(IOException e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            if (se.getState() == ShopSateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
                System.out.println(modelMap.get("success"));
                return modelMap;
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
                System.out.println(modelMap.get("success"));
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺id");
            System.out.println(modelMap.get("success"));
            return modelMap;
        }
        //3、返回结果
    }


}
