package com.zxc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zxc.o2o.dto.UserAwardMapExecution;
import com.zxc.o2o.entity.Award;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.UserAwardMap;
import com.zxc.o2o.enums.UserAwardMapStateEnum;
import com.zxc.o2o.service.AwardService;
import com.zxc.o2o.service.PersonInfoService;
import com.zxc.o2o.service.UserAwardMapService;
import com.zxc.o2o.util.CodeUtil;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 19:05
 * @Version 1.0
 *
 */
@Controller
@RequestMapping("frontend")
public class AwardController {

    @Autowired
    private UserAwardMapService userAwardMapService;

    @Autowired
    private AwardService awardService;

    @Autowired
    private PersonInfoService personInfoService;

    /*
    * 在线兑换礼品 添加映射
    * */
    @RequestMapping(value = "adduserawardmap",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addUserAwardMap(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //从session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //从前端请求中获取奖品Id
        Long awardId = HttpServletRequestUtil.getLong(request,"awardId");
        //获取店铺Id
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        //获取兑换奖品所需要的积分
        Integer awardPoint = HttpServletRequestUtil.getInt(request,"point");
        //封装成用户奖品映射对象
        UserAwardMap userAwardMap = compactUserAwardMap4Add(user,awardId,shopId,awardPoint);
        //空值判断
        if (userAwardMap != null){
            try {
                //添加兑换信息
                UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
                if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请选择领取的奖品");
        }
        return modelMap;
    }

    /*
    * 获取顾客的兑换列表
    * */
    @RequestMapping(value = "/listuserawardmapsbycustomer",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listUserAwardMapsByCustomer(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize= HttpServletRequestUtil.getInt(request,"pageSize");
        //session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && user != null && user.getUserId() != null){
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            userAwardMapCondition.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request,"shopId");
            if (shopId > -1){
                //若店铺Id为非空，则将其添加进查询条件，即查询该用户在某个店铺下的兑换信息
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userAwardMapCondition.setShop(shop);
            }
            String awardName = HttpServletRequestUtil.getString(request,"awardName");
            if (awardName != null){
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }

            //根据传入的查询条件分页的显示用户奖品映射信息
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition,pageIndex,pageSize);
            modelMap.put("userAwardMapList",ue.getUserAwardMapList());
            modelMap.put("count",ue.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or userId");
        }
        return modelMap;
    }

    /*
    * 根据用户奖品映射Id获取单条顾客奖品的映射信息
    * */
    @RequestMapping(value = "/getawardbyuserawardid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getAwardById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取前端传递过来的userAwardId
        long userAwardId = HttpServletRequestUtil.getLong(request,"userAwardId");
        //空值判断
        if (userAwardId > -1){
            //根据Id获取顾客奖品的映射信息，进而领取奖品Id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            //根据奖品Id获取奖品信息
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            //将奖品信息和奖品状态返回给前端
            modelMap.put("award",award);
            modelMap.put("usedStatus",userAwardMap.getUsedStatus());
            modelMap.put("userAwardMap",userAwardMap);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty awardId");
        }
        return modelMap;
    }

    //微信获取用户信息的api前缀
    private static String urlPrefix;
    //微信获取用户api的中间部分
    private static String urlMiddle;
    //微信获取用户信息的api后缀
    private static String urlSuffix;
    //微信回传给的响应添加顾客商品映射的信息的url
    private static String exchangeUrl;


    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        this.urlMiddle = urlMiddle;
    }
    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }
    @Value("${wechat.productmap.url}")
    public void setProductmapUrl(String exchangeUrl) {
        this.exchangeUrl = exchangeUrl;
    }

    /*
     * 生成奖品的消费凭证,供操作员扫描，证明已经消费，微信扫一扫就能链接对应的URL里
     * */
    @RequestMapping(value = "/generateqrcode4award",method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response){
        //获取前端传递过来的商品Id
        long userAwardId = HttpServletRequestUtil.getLong(request,"userAwardId");
        //从Session中获取当前顾客的信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //根据Id获取顾客奖品映射实体类对象
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        //空值判断
        if (userAwardMap != null && user != null && user.getUserId() != null
        && userAwardMap.getUser().getUserId() == user.getUserId()){
            //获取当前时间戳，以保证二维码的时间的有效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            //将奖品id，顾客Id和timeStamp传入content，赋值到state中，这样微信获取到这些信息后回传到用户商户
            //加上aaa是为了在添加信息的方法里替换到这些信息
            String content = "{aaauserAwardIdaaa:"+userAwardId
                    +",aaacustomerIdaaa:"+user.getUserId()+",aaacreateTimeaaa:"+timeStamp+"}";
            try {
                //将content信息先进行编码以避免特殊字符造成的干扰，之后拼接出目标URL
                String loginUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content,"UTF-8")+urlSuffix;
                //将目标URL转换成短的URL(暂时不使用)
                //调用二维码生成的工具类方法，生成二维码
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(loginUrl,response);
                //将二维码以文件流的方式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg,"png",response.getOutputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/getawardbyawardid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getAwardByAwardId(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取awardId
        Long awardId = HttpServletRequestUtil.getLong(request,"awardId");
        if (awardId != null){
            Award award = awardService.getAwardById(awardId);
            if (award != null){
                modelMap.put("success",true);
                modelMap.put("award",award);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","获取奖品信息为空！");
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","无该奖品！");
        }
        return modelMap;
    }


    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId, Long currentShop,Integer awardPoint) {
        UserAwardMap userAwardMap = new UserAwardMap();
        if (currentShop != null){
            Shop shop = new Shop();
            shop.setShopId(currentShop);
            userAwardMap.setShop(shop);
        }
        if (user != null){
            userAwardMap.setUser(user);
        }
        if (awardId != null){
            Award award = new Award();
            award.setAwardId(awardId);
            userAwardMap.setAward(award);
        }
        if (awardPoint != null){
            userAwardMap.setPoint(awardPoint);
        }
        return userAwardMap;
    }



}
