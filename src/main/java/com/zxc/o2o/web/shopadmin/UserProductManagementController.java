package com.zxc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.o2o.dao.ProductSellDailyDao;
import com.zxc.o2o.dto.*;
import com.zxc.o2o.entity.*;
import com.zxc.o2o.enums.UserProductMapStateEnum;
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
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 22:40
 * @Version 1.0
 *
 */
@Controller
@RequestMapping(value = "/shopadmin")
public class UserProductManagementController {

    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listuserproductmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listUserProductMapsByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        //空值判断，主要保证shopId不为空
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            UserProductMap userProductMapCondition = new UserProductMap();

            userProductMapCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                //若前端想要按照商品名模糊查询，则传入productName
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            //根据传入的查询条件获取该店铺的商品销售情况
            UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
            modelMap.put("userProductMapList", ue.getUserProductMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        //获取当前店铺id
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //确保shopId不为空
        if ((currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);

            Calendar calendar = Calendar.getInstance();
            //获取昨日的日期
            calendar.add(Calendar.DATE,-1);
            Date endTime = calendar.getTime();
            //获取七天前的日期
            calendar.add(Calendar.DATE,-6);
            Date beginTime = calendar.getTime();

            //根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition,beginTime,endTime);
            //指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //商品名列表，保证唯一性
            HashSet<String> legendData = new HashSet<>();
            //x轴数据
            HashSet<String> xData = new HashSet<>();
            //定义series
            List<EchartSeries> series = new ArrayList<>();
            //日销量列表
            List<Integer> totalList = new ArrayList<>();
            //当前商品名，默认为空
            String currentProductName = "";
            for (int i = 0;i < productSellDailyList.size();i++){
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                //自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) &&
                        !currentProductName.isEmpty()){
                    //如果currentProductName不等于获取的商品名，或者已遍历到列表的末尾，且currentProductName不为空
                    //则遍历到下一个商品的日销量信息了，将前一轮遍历的信息放入到series当中
                    //包括商品以及商品对应的统计日期以及当天的销量
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0,totalList.size()));
                    series.add(es);
                    //重置totalList
                    totalList = new ArrayList<>();
                    //变换一下currentProductId为当前的productId
                    currentProductName = productSellDaily.getProduct().getProductName();
                    //继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                }else {
                    //如果当前的值还是当前的则继续添加
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }

                //队列，需要将最后的一个商品销量信息也添加上
                if (i == productSellDailyList.size()-1){
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0,totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series",series);
            modelMap.put("legendData",legendData);
            //拼接出xAxis
            List<EchartXAxis> xAxis = new ArrayList<>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis",xAxis);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/adduserproductmap",method = RequestMethod.GET)
    private String addUserProductMap(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //获取微信授权信息
        WechatAuth auth = getOperatorInfo(request);
        if (auth != null){
            PersonInfo operator = auth.getPersonInfo();
            request.getSession().setAttribute("user",operator);
            //获取二维码里state携带的content信息并解码
            String qeCodeInfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request,"state"),"UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //将解码的内容去掉aaa，
                wechatInfo = mapper.readValue(qeCodeInfo.replace("aaa","/"),WechatInfo.class);
            }catch (Exception e){
                return "shop/operationfail";
            }

            //校验二维码是否过期
            if(!checkQRCodeInfo(wechatInfo)){
                return "shop/operationfail";
            }

            //获取添加消费记录所需的参数并组成userproductmap实例
            Long productId = wechatInfo.getProductId();
            Long customerId = wechatInfo.getCustomerId();
            UserProductMap userProductMap = compactUserProductMap4Add(customerId,productId,auth.getPersonInfo());

            //空值校验
            if (userProductMap != null && customerId != -1){
                try {
                    if (!checkShopAuth(operator.getUserId(),userProductMap)){
                        return "shop/operationfail";
                    }
                    //添加消费记录
                    UserProductMapExecution se = userProductMapService.addUserProductMap(userProductMap);
                    if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()){
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
    private boolean checkShopAuth(Long userId, UserProductMap userProductMap) {
        //获取该店铺下的所以授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.listShopAuthMapByShopId(userProductMap.getShop().getShopId(),1,1000);
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
    private UserProductMap compactUserProductMap4Add(Long customerId, Long productId, PersonInfo personInfo) {
        UserProductMap userProductMap = null;
        if (customerId != null && productId != null){
            userProductMap = new UserProductMap();
            PersonInfo customer = new PersonInfo();
            personInfo.setUserId(customerId);
            userProductMap.setUser(customer);
            //为了获取商品积分
            Product product = productService.getProductById(productId);
            userProductMap.setProduct(product);
            userProductMap.setShop(product.getShop());
            userProductMap.setPoint(product.getPoint());
            userProductMap.setCreateTime(new Date());
        }
        return userProductMap;
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

}
