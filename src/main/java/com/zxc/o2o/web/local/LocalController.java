package com.zxc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 17:20
 * @Version 1.0
 *
 */
@Controller
@RequestMapping(value = "/local")
public class LocalController {
    //微信绑定
    @RequestMapping(value = "/wechatbind")
    public String wechatBind(){
        return "local/wechatbind";
    }

    //修改密码
    @RequestMapping(value = "/changepsw")
    public String changePwd(){
        return "local/changepsw";
    }

    //登录页面路由
    @RequestMapping(value = "/login")
    public String login(){
            return "local/login";
    }

    //注册用户路由
    @RequestMapping(value = "/registry")
    public String registry(){
        return "local/registry";
    }
}
