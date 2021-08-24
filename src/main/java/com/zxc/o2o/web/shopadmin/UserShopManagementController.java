package com.zxc.o2o.web.shopadmin;

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

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 22:08
 * @Version 1.0
 *
 */
@Controller
@RequestMapping(value = "shopadmin")
public class UserShopManagementController {

    @Autowired
    private UserShopMapService userShopMapService;

    @RequestMapping(value = "/listusershopmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listUserShopMapsByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        //空值判断，主要保证shopId不为空
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            UserShopMap userShopMapCondition = new UserShopMap();
            userShopMapCondition.setShop(currentShop);
            //搜索某人的在某店铺的记录
            String name = HttpServletRequestUtil.getString(request, "name");
            if (name != null) {
                //若前端想要按照顾客名模糊查询，则传入name
                PersonInfo user = new PersonInfo();
                user.setName(name);
                userShopMapCondition.setUser(user);
            }
            //根据传入的查询条件获取该店铺的商品销售情况
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
