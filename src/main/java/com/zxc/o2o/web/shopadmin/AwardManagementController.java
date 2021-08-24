package com.zxc.o2o.web.shopadmin;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 10:37
 * @Version 1.0
 *
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.o2o.dto.AwardExecution;
import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.entity.Award;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.enums.AwardStateEnum;
import com.zxc.o2o.exceptions.AwardOperationException;
import com.zxc.o2o.service.AwardService;
import com.zxc.o2o.util.CodeUtil;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class AwardManagementController {

    @Autowired
    private AwardService awardService;

    @RequestMapping(value = "/listawardsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listAwardsByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();

        //获取前台传过来的页码  暂时使用地址栏传值分页范围
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取前台传过来的每页要求返回的奖品数上限
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从当前session中获取店铺信息，主要是获取awardId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            //判断是否传入奖品名
            String awardName = HttpServletRequestUtil.getString(request,"awardName");
            //拼接查询条件
            Award awardCondition = compactAwardConditionSearch(currentShop.getShopId(),awardName);
            //根据传过来的shopAdmin判断是否需要判断奖品是否为上架状态
            String shopAdmin = HttpServletRequestUtil.getString(request,"shopAdmin");
            if (shopAdmin == null){
                shopAdmin = "0";
            }
            AwardExecution ae = awardService.getAwardList(awardCondition,pageIndex,pageSize,shopAdmin);
            modelMap.put("awardList", ae.getAwardList());
            modelMap.put("count", ae.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    //获取当前奖品编辑的信息
    @RequestMapping(value = "/getawardbyid")
    @ResponseBody
    private Map<String,Object> getAwardById(@RequestParam Long awardId){
        Map<String,Object> modelMap = new HashMap<>();
        if(awardId != null){
            Award award = awardService.getAwardById(awardId);
            modelMap.put("award",award);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty awardId");
        }
        return modelMap;
    }

    //奖品编辑
    @RequestMapping(value = "/modifyaward",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyAward(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //是奖品编辑的时候调用还是上下架操作的时候调用
        //若为奖品编辑的时候进行验证码判断，后者就可以跳过验证码验证
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        //接收前端参数的变量的初始化，包括奖品，缩略图，奖品详情图列表实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Award award = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> awardImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
        if (multipartResolver != null){
            try {
                //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
                if (multipartResolver.isMultipart(request)) {
                    thumbnail = handleImage(request,thumbnail,awardImgList);
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        try {
            String awardStr = HttpServletRequestUtil.getString(request,"awardStr");
            //尝试获取前端传过来的表单string流并将其转换成Award实体类
            award = objectMapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }

        //非空判断
        if (award != null){
            try {
                //从Session中获取当前店铺的Id并赋值给award，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());

                //开始进行奖品信息的变更操作
                AwardExecution awardExecution = awardService.modifyAward(award,thumbnail,awardImgList);
                if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",awardExecution.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入奖品信息");
        }
        return modelMap;
    }

    //奖品添加
    @RequestMapping(value = "/addaward", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if (CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
        }

        //接收前端参数的变量的初始化，包括奖品，缩略图，奖品详情图列表实体类
        ObjectMapper objectMapper = new ObjectMapper();
        Award award = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> awardImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

       if (multipartResolver != null){
           try {
               //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
               if (multipartResolver.isMultipart(request)) {
                   thumbnail = handleImage(request,thumbnail,awardImgList);
               }
               if (thumbnail ==null){
                   modelMap.put("success", false);
                   modelMap.put("errMsg", "上传图片不能为空");
                   return modelMap;
               }
           } catch (Exception e) {
               modelMap.put("success", false);
               modelMap.put("errMsg", e.toString());
               return modelMap;
           }
       }else {
           modelMap.put("success", false);
           modelMap.put("errMsg", "上传图片不能为空");
           return modelMap;
       }
        try {
            String awardStr = HttpServletRequestUtil.getString(request,"awardStr");
            //尝试获取前端传过来的表单string流并将其转换成award实体类
            award = objectMapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        //若award信息，缩略图以及详情图列表为非空，则开始进行奖品添加操作
        if (award != null && thumbnail != null && awardImgList.size() > 0) {
            try {
                //从session中获取当前店铺的Id并赋值给award，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());

                //执行添加操作
               AwardExecution pe = awardService.addAward(award, thumbnail, awardImgList);
                if (pe.getState() ==AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (AwardOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入奖品信息");
        }
        return modelMap;
    }

    private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> awardImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //取出缩略图并构建的ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        ImageHolder awardImg = null;
        if (thumbnailFile != null){
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
            awardImg = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
        }else {
            return null;
        }
        if (awardImg != null){
            awardImgList.add(awardImg);
        }
        return thumbnail;
    }

    /*
     * 封装奖品查询条件到Award实例中
     *
     * awardId(mandatory)
     * awardCategoryId(optional)
     * awardName(optional)
     * */
    private Award compactAwardConditionSearch(Long shopId,String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        //若有奖品名模糊查询的要求则添加进去
        if (awardName != null){
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }

}
