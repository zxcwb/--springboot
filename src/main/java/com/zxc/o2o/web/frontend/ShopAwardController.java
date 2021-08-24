package com.zxc.o2o.web.frontend;

import com.zxc.o2o.dto.AwardExecution;
import com.zxc.o2o.entity.Award;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.UserShopMap;
import com.zxc.o2o.service.AwardService;
import com.zxc.o2o.service.UserShopMapService;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 18:49
 * @Version 1.0
 *
 */
@Controller
@RequestMapping(value = "frontend")
public class ShopAwardController {

    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;

    @RequestMapping(value = "/listawardsbyshop")
    @ResponseBody
    private Map<String,Object> listAwardsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();

        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //获取店铺Id
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)){
            //获取前端可输入的奖品名进行模糊查询
            String awardName = HttpServletRequestUtil.getString(request,"awardName");
            Award awardCondition = compactAwardConditionSearch(shopId,awardName);
            //传入查询条件分页获取奖品信息
            //根据传过来的shopAdmin判断是否需要判断奖品是否为上架状态
            String shopAdmin = HttpServletRequestUtil.getString(request,"shopAdmin");
            if (shopAdmin == null){
                shopAdmin = "0";
            }
            AwardExecution ae = awardService.getAwardList(awardCondition,pageIndex,pageSize,shopAdmin);
            modelMap.put("awardList",ae.getAwardList());
            modelMap.put("count",ae.getCount());
            modelMap.put("success",true);

            //从Session中获取用户信息，主要为了显示该用户在本店铺的积分
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            //空值判断
            if (user != null && user.getUserId() != null){
                //获取该用户在本店铺的积分信息
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(),shopId);
                if (userShopMap == null){
                    modelMap.put("totalPoint",0);
                }else {
                    modelMap.put("totalPoint",userShopMap.getPoint());
                }
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or ShopId");
        }
        return modelMap;
    }

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
