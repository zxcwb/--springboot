package com.zxc.o2o.interceptor.shopadmin;

import com.zxc.o2o.entity.Shop;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/6 7:14
 * @Version 1.0
 * 店家管理系统操作验证拦截器
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        @SuppressWarnings("unchecked")
        //从session中获取当前用户可操作的店铺列表
        List<Shop> shopList  = (List<Shop>) request.getSession().getAttribute("shopList");
        if (currentShop != null && shopList != null){
            //遍历可操作的店铺列表
            for (Shop shop : shopList){
                //如果当前店铺在可操作的列表里返回true，则进行接下来的用户操作
                if (shop.getShopId() == currentShop.getShopId()){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
