package com.zxc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zxc.o2o.dto.ShopAuthMapExecution;
import com.zxc.o2o.dto.WechatInfo;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.ShopAuthMap;
import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.enums.ShopAuthMapStateEnum;
import com.zxc.o2o.service.PersonInfoService;
import com.zxc.o2o.service.ShopAuthMapService;
import com.zxc.o2o.service.WechatAuthService;
import com.zxc.o2o.util.CodeUtil;
import com.zxc.o2o.util.HttpServletRequestUtil;
import com.zxc.o2o.util.longtoshort.ShortNetAddressUrl;
import com.zxc.o2o.util.wechat.WechatUserUtil;
import com.zxc.o2o.util.wechat.message.pojo.UserAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/12 9:01
 * @Version 1.0
 *
 */
@Controller
@RequestMapping(value = "shopadmin")
public class ShopAuthManagementController {

    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private WechatAuthService wechatAuthService;

    //微信获取用户信息的api前缀
    private static String urlPrefix;
    //微信获取用户信息的api中间部分
    private static String urlMiddle;
    //微信获取用户信息的api后缀
    private static String urlSuffix;
    //微信回传给的响应添加授权信息的url
    private static String authUrl;


    @RequestMapping(value = "/listshopauthmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //取出分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            //分页取出该店铺下的授权信息列表
            ShopAuthMapExecution se = shopAuthMapService
                    .listShopAuthMapByShopId(currentShop.getShopId(), pageIndex, pageSize);
            modelMap.put("shopAuthMapList", se.getShopAuthMapList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (shopAuthId != null && shopAuthId > -1) {
            //根据前台传入的shopAuthId查找对应的授权信息
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            modelMap.put("shopAuthMap", shopAuthMap);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopAuthId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //是授权编辑时候调用，还是删除/恢复授权操作的时候调用 ==> 改变enableStatus
        //若为前者则进行验证码判断，后者跳过验证码
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)&&!statusChange) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        //空值判断
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                //看看被操作的对方是否为店家本身，店家本身不支持修改
                if (!checkPermission(shopAuthMap.getShopAuthId())){
                    modelMap.put("success",false);
                    modelMap.put("errMsg","无法对店家本身进行权限操作！已是最高权限!");
                    return modelMap;
                }

                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                shopAuthMap.getShop().setShopId(currentShop.getShopId());
                shopAuthMap.getEmployee().setUserId(user.getUserId());
                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);

                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }

            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入要修改的授权信息");
        }
        return modelMap;
    }


    /*
    * 根据微信回传回来的产参数添加店铺授权信息
    * */
    @RequestMapping(value = "/addshopauthmap", method = RequestMethod.POST)
    private String addShopAuthMap(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
       //从request里获取微信用户的信息
        WechatAuth auth = getEmployeeInfo(request);
        if (auth != null){
            //根据userId 获取微信用户的信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //将用户信息添加进user里
            request.getSession().setAttribute("user",user);
            //解析微信回传来的自定义参数state，由于之前进行了编码，这里需要解码
            String qrCodeInfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request,"state"),"UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //将解码之后的内容用aaa替换掉之前生成的二维码的时候加入的aaa前缀，转换成WechatInfo实体类
                wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa","/"),WechatInfo.class);
            }catch (Exception e){
                return "shop/operationfail";
            }

            //校验二维码是否已经过期
            if (!checkQRCodeInfo(wechatInfo)){
                return "shop/operationfail";
            }

            //去重校验
            //获取该店铺下所有的店铺信息
            ShopAuthMapExecution allMapList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(),1,999);
            List<ShopAuthMap> shopAuthMapList = allMapList.getShopAuthMapList();
            for (ShopAuthMap sm : shopAuthMapList){
                if (sm.getEmployee().getUserId() == user.getUserId()){
                    return "shop/operationfail";
                }
            }

            try {
                //根据获取到的内容，添加微信授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);
                PersonInfo employee = new PersonInfo();
                shopAuthMap.setEmployee(employee);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()){
                    return "shop/operationsuccess";
                }else {
                    return "shop/operationfail";
                }
            }catch (RuntimeException e){
                return "shop/oeprationfail";
            }
        }
        return "shop/operationfail";
    }

    /*
    * 根据二维码携带的createTime判断其是否超过十分钟，超过十分钟则认为过期
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

    private WechatAuth getEmployeeInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code){
            UserAccessToken token;
            try {
                token = WechatUserUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId",openId);
                auth = wechatAuthService.findWechatAuthByOpenId(openId);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return auth;
    }
   /* @RequestMapping(value = "/addshopauthmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (shopAuthMap != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute(
                        "currentShop");
                PersonInfo user = (PersonInfo) request.getSession()
                        .getAttribute("user");
                if (currentShop.getOwnerId() != user.getUserId()) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无操作权限");
                    return modelMap;
                }
                shopAuthMap.getShop().setShopId(currentShop.getShopId());
                shopAuthMap.getEmployee().setUserId(user.getUserId());
                ShopAuthMapExecution se = shopAuthMapService
                        .addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入授权信息");
        }
        return modelMap;
    }*/


    @RequestMapping(value = "/removeshopauthmap", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> removeShopAuthMap(Long shopAuthId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (shopAuthId != null && shopAuthId > 0) {
            try {
                ShopAuthMapExecution se = shopAuthMapService
                        .removeShopAuthMap(shopAuthId);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个授权进行删除");
        }
        return modelMap;
    }

    /*
    * 检查被操作对象是否可以被修改   是否为店家判断
    * */
    private boolean checkPermission(Long shopAuthId) {
        ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
        if (grantedPerson.getTitleFlag() == 0){
            //若是店家本身，不可操作
            return false;
        }else {
            return true;
        }
    }

    @Value("${wechat.prefix}")
    public void setPrefix(String prefix) {
        ShopAuthManagementController.urlPrefix = prefix;
    }


    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    /*
    * 生成带有URL的二维码，微信扫一扫就能链接到对应的url里面
    * */
    @RequestMapping(value = "/generateqrcode4shopauth",method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response){
        //从session里获取当前shop的信息
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop != null && shop.getShopId() != null){
            //获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            //将店铺id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到授权信息的添加
            //加上aaa是为了在添加信息的方法里替换掉这些信息使用
            String content = "{aaashopIdaaa:"+shop.getShopId()+",aaacreateTimeaaa:"+timeStamp+"}";
            try {
                //将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接出目标URL
                String loginUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content,"UTF-8") + urlSuffix;
                //该方法暂时无法使用（实现） TODO 将目标URL转换成短URL
                //String shortUrl = ShortNetAddressUrl.getShortURL(loginUrl);
                //调用二维码生成工具类方法，传入短的URL，生成二维码
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(loginUrl,response);
                //将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg,"png",response.getOutputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
