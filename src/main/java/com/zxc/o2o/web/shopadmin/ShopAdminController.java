package com.zxc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shop",method = RequestMethod.GET)
public class ShopAdminController {


    @RequestMapping(value = "/shopedit")
    public String shopEdit(){
        return "shop/shopedit";
    }

    @RequestMapping(value = "/shopregistry")
    public String shopRegistry(){
        return "shop/shopregistry";
    }


    @RequestMapping(value = "/shopmanage")
    public String shopManage(){
        return "shop/shopmanage";
    }


    @RequestMapping(value = "/shoplist")
    public String shoplist(){
        System.out.println("shop/shoplist");
        return "shop/shoplist";
    }

    @RequestMapping(value = "/productcategorymanage")
    public String productCategoryManage(){
        //商品类型管理
        return "shop/productcategorymanage";
    }

    //商品信息编辑/添加
    @RequestMapping(value = "/productedit")
    public String productOperation(){
        //转发到商品添加/编辑页面
        return "shop/productedit";
    }

    //商品管理
    @RequestMapping(value = "/productmanage")
    public String productmanage(){
        return "shop/productmanage";
    }

    //授权管理
    @RequestMapping(value = "/shopauthmanage")
    public String shopauthmanage(){
        return "shop/shopauthmanage";
    }

    //授权信息修改
    @RequestMapping(value = "/shopauthedit")
    public String shopAuthEdit(){
        return "shop/shopauthedit";
    }

    //授权失败
    @RequestMapping(value = "/operationfail")
    public String operationfail(){
        return "shop/operationfail";
    }

    //授权成功
    @RequestMapping(value = "/operationsuccess")
    public String operationsuccess(){
        return "shop/operationsuccess";
    }

    //消费记录
    @RequestMapping(value = "/productbuycheck")
    public String productbuycheck(){
        return "shop/productbuycheck";
    }

    //顾客积分
    @RequestMapping(value = "/usershopcheck")
    public String usershopcheck(){
        return "shop/usershopcheck";
    }

    //积分兑换
    @RequestMapping(value = "/awarddelivercheck")
    public String awarddelivercheck(){
        return "shop/awarddelivercheck";
    }

    //奖品管理
    @RequestMapping(value = "/awardmanage")
    public String awardmanage(){
        return "shop/awardmanage";
    }

    //奖品修改
    @RequestMapping(value = "/awardedit")
    public String awardedit(){
        return "shop/awardedit";
    }

}
