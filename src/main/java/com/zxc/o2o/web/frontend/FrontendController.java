package com.zxc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "frontend/index";
    }

    //店铺类别列表路口
    @RequestMapping(value = "/shoplist",method = RequestMethod.GET)
    public String showShopList(){
        return "frontend/shoplist";
    }

    //店铺详情页路由
    @RequestMapping(value = "/shopdetail",method = RequestMethod.GET)
    public String showShopDetail(){
        return "frontend/shopdetail";
    }


    //商品详情页路由
    @RequestMapping(value = "/productdetail",method = RequestMethod.GET)
    public String showProductDetail(){
        return "frontend/productdetail";
    }

    //奖品兑换路由
    @RequestMapping(value = "/awardlist",method = RequestMethod.GET)
    public String awardList(){
        return "frontend/awardlist";
    }

    //奖品兑换记录（积分兑换）路由
    @RequestMapping(value = "/pointrecord",method = RequestMethod.GET)
    public String showPointRecord(){
        return "frontend/pointrecord";
    }

    //奖品详情页路由
    @RequestMapping(value = "/awarddetail",method = RequestMethod.GET)
    public String awarddetail(){
        return "frontend/awarddetail";
    }

    //消费记录列表路由
    @RequestMapping(value = "/myrecord",method = RequestMethod.GET)
    public String showMyRecord(){
        return "frontend/myrecord";
    }

    //用户在各店铺的积分情况路由
    @RequestMapping(value = "/mypoint",method = RequestMethod.GET)
    public String showMyPoint(){
        return "frontend/mypoint";
    }


}
