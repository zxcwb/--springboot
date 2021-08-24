package com.zxc.o2o.web.wechat;

/*
* 编写5个类，来获取关注此公众号的用户信息
*
* WechatLoginController、UserAccessToken、WechatUser、WechatUtil、MyX666TrustManager
* */


import com.zxc.o2o.dto.WechatAuthExecution;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.WechatAuth;
import com.zxc.o2o.enums.WechatAuthStateEnum;
import com.zxc.o2o.service.PersonInfoService;
import com.zxc.o2o.service.WechatAuthService;
import com.zxc.o2o.util.wechat.WechatUserUtil;
import com.zxc.o2o.util.wechat.message.pojo.UserAccessToken;
import com.zxc.o2o.util.wechat.message.pojo.WechatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//用来获取已关注此微信号的用户信息并做相应的处理
//关注公众号之后的微信用户信息接口，如果在微信浏览器里访问
//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb48db798b844ceda&redirect_uri=http://zxcservice.xyz/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf2d57359b1120b1e&redirect_uri=http://zxcservice.xyz/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect

@Controller
@RequestMapping(value = "wechatlogin")
public class WechatLoginController {

    private static Logger logger = LoggerFactory.getLogger(WechatLoginController.class);
    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/logincheck", method = RequestMethod.GET)
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("wechat login get...");
        //获取微信公众号传输过来的code，通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");

        //这个state可以用来传输我们自定义的信息，方便程序调试，这里也可以不用
        //判断当前的角色类型是啥，有两个按钮，一个是前端展示系统，一个是店家管理页面
        String roleType = request.getParameter("state");
        logger.debug("wechat login code:" + code);

        WechatUser user = null;
        String openId = null;
        WechatAuth wechatAuth = null;
        if (null != code) {
            UserAccessToken token;

            try {
                //通过code获取access_token
                token = WechatUserUtil.getUserAccessToken(code);
                logger.debug("wechat login token:" + token.toString());

                //通过token获取accessToken
                String accessToken = token.getAccessToken();

                //通过token获取openId
                openId = token.getOpenId();

                //通过access_token和openId获取用户昵称等信息
                user = WechatUserUtil.getUserInfo(accessToken, openId);
                logger.debug("wechat login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);
                wechatAuth = wechatAuthService.findWechatAuthByOpenId(openId);
            } catch (IOException e) {
                logger.error("error in getUserAccessToken or getUserInfo or findByOpenId:" + e.toString());
                e.printStackTrace();
            }
        }

        // === to do begin ===
        //前面获取到openId之后，可以通过它去数据库判断该微信账号是否在我们网站里有对应的账号了，没有的话这里可以自动创建上，直接实现
        //微信与我们的网站的无缝对接,首次登录
        // === to do end ===
        if (wechatAuth == null){
            //从微信返回的信息里面提取提取数据库需要的字段信息
            PersonInfo personInfo = WechatUserUtil.getPersonInfoFromRequest(user);
            wechatAuth = new WechatAuth();
            wechatAuth.setPersonInfo(personInfo);
            if (FRONTEND.equals(roleType)){
                personInfo.setUserType(1);
            }else {
                personInfo.setUserType(2);
            }

            wechatAuth.setOpenId(openId);
            WechatAuthExecution wechatAuthExecution = wechatAuthService.register(wechatAuth);

            if (wechatAuthExecution.getState() != WechatAuthStateEnum.SUCCESS.getState()){
                return null;
            }else {
                personInfo = personInfoService.getPersonInfoById(wechatAuth.getPersonInfo().getUserId());
                request.getSession().setAttribute("user",personInfo);
            }
        }

        //若用户点击的是前端展示系统按钮则进入前端展示系统
        if (FRONTEND.equals(roleType)){
            return "frontend/index";
        }else {
            return "shop/shoplist";
        }

    }
}
