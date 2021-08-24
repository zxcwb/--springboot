package com.zxc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.o2o.dto.ShopAuthMapExecution;
import com.zxc.o2o.dto.UserAwardMapExecution;
import com.zxc.o2o.dto.WechatInfo;
import com.zxc.o2o.entity.*;
import com.zxc.o2o.enums.UserAwardMapStateEnum;
import com.zxc.o2o.service.*;
import com.zxc.o2o.util.HttpServletRequestUtil;
import com.zxc.o2o.util.wechat.WechatUserUtil;
import com.zxc.o2o.util.wechat.message.pojo.UserAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 8:49
 * @Version 1.0
 *
 */
@Controller
@RequestMapping("shopadmin")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listuserawardmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listUserAwardMapsByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        //空值判断，主要保证shopId不为空
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            UserAwardMap UserAwardMapCondition = new UserAwardMap();
            UserAwardMapCondition.setShop(currentShop);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                //若前端想要按照商品名模糊查询，则传入awardName
                Award award = new Award();
                award.setAwardName(awardName);
                UserAwardMapCondition.setAward(award);
            }
            //根据传入的查询条件获取该店铺的商品销售情况
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(UserAwardMapCondition, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ue.getUserAwardMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    /*
    * 操作员扫描二维码之后，改变奖品状态，表示以及领取过了
    * */
    @RequestMapping(value = "/exchangeaward",method = RequestMethod.GET)
    private String exchangeAward(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //获取负责扫二维码的店员信息
        WechatAuth auth = getOperatorInfo(request);
        if (auth != null){
            //通过userId获取店员信息
            PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //设置用户的session
            request.getSession().setAttribute("user",operator);

            //解析微信回传回来的state,需要先解析
            String qrCodeInfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request,"state"),"UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //解码后，去掉aaa
                wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa","/"),WechatInfo.class);
            }catch (Exception e){
                return "shop/operationfail";
            }

            //检验是否过期
            if(!checkQRCodeInfo(wechatInfo)){
                return "shop/operationfail";
            }

            //获取用户奖品映射主键
            Long userAwardId = wechatInfo.getUserAwardId();
            //获取顾客Id
            Long customerId = wechatInfo.getCustomerId();
            //将顾客信息，操作员信息以及奖品信息封装成userAwardMap
            UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId,userAwardId,operator);
            if (userAwardMap != null){
                try {
                    //是否有权限操作员
                    if (!checkShopAuth(operator.getUserId(),userAwardMap)){
                        return "shop/operationfail";
                    }
                    //修改奖品状态
                    UserAwardMapExecution se = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()){
                        return "shop/operationsuccess";
                    }
                }catch (RuntimeException e){
                    return "shop/operationfail";
                }
            }
        }
        return "shop/operationfail";
    }


    /*
     * 检查操作员是否有权限
     * */
    private boolean checkShopAuth(Long userId, UserAwardMap userAwardMap) {
        //获取该店铺下的所以授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(),1,1000);
        for (ShopAuthMap shopAuthMap:shopAuthMapExecution.getShopAuthMapList()){
            //看看是否给该人员设置过权限
            if(shopAuthMap.getEmployee().getUserId() == userId){
                return true;
            }
        }
        return false;
    }


    /*
     * 根据传入的customerId，productId，以及操作员的信息组建用户消费记录
     * */
    private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo personInfo) {
        UserAwardMap userAwardMap = null;
        if (customerId != null && userAwardId != null){
            userAwardMap = new UserAwardMap();
            PersonInfo customer = new PersonInfo();
            personInfo.setUserId(customerId);
            userAwardMap.setUser(customer);
            //为了获取商品积分
            UserAwardMap  u = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setAward(u.getAward());
            userAwardMap.setShop(u.getShop());
            userAwardMap.setPoint(u.getPoint());
            userAwardMap.setCreateTime(new Date());
        }
        return userAwardMap;
    }


    /*
     *通过code过去UserAccessToken，进而通过token里的openId获取微信用户信息
     * */
    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code){
            UserAccessToken token;
            try {
                token = WechatUserUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId",openId);
                auth = wechatAuthService.findWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }

    /*
     * 检查二维码是否过期
     * */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null){
            long nowTime = System.currentTimeMillis();
            if ((nowTime - wechatInfo.getCreateTime()) <= 600000){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

}
