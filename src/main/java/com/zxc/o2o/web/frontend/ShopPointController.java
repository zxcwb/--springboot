package com.zxc.o2o.web.frontend;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/16 9:44
 * @Version 1.0
 *
 */

import com.zxc.o2o.dto.UserProductMapExecution;
import com.zxc.o2o.dto.UserShopMapExecution;
import com.zxc.o2o.entity.*;
import com.zxc.o2o.service.UserShopMapService;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("frontend")
public class ShopPointController {

    @Autowired
    private UserShopMapService userShopMapService;

    /*
    * 列出用户积分情况
    *
    * */
    @RequestMapping(value = "/listusershopmapsbycustomer",method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {
            Map<String, Object> modelMap = new HashMap<>();
            //获取分页信息
            int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
            int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

            //从session里获取顾客信息
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            //空值判断
            if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != -1) {
                UserShopMap userShopMapCondition = new UserShopMap();
                userShopMapCondition.setUser(user);
                long shopId = HttpServletRequestUtil.getLong(request, "shopId");
                String shopName = HttpServletRequestUtil.getString(request,"shopName");
                Shop shop = new Shop();
                if (shopId > -1) {
                    //若传入的店铺信息，则列出某个店铺下该顾客的积分情况
                    shop.setShopId(shopId);
                }
                if (shopName != null){
                    shop.setShopName(shopName);
                }
                userShopMapCondition.setShop(shop);
                //根据查询分页信息返回用户消费记录
                UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
                modelMap.put("userShopMapList", ue.getUserShopMapList());
                modelMap.put("count", ue.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
            }
            return modelMap;
        }

}

