package com.zxc.o2o.web.wechat;

import com.zxc.o2o.util.wechat.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping(value = "/wechat")
public class WechatController {

    private static Logger logger = LoggerFactory.getLogger(WechatController.class);

    //进入此Controller自动调用doGet方法
    @ResponseBody
    @RequestMapping(value = "/config",method = RequestMethod.GET,produces = { "application/json;charset=utf-8" })
    public String WechatInterfaceConfiguration(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("wechat get...");
        String signature = request.getParameter("signature");/// 微信加密签名
        String timestamp = request.getParameter("timestamp");/// 时间戳
        String nonce = request.getParameter("nonce"); /// 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                logger.debug("wechat get success....");
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
                out=null;
        }
        return echostr;
    }

    /*public void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("wechat 配置开始......");
        logger.debug("wechat get...");
        String signature = request.getParameter("signature");/// 微信加密签名
        String timestamp = request.getParameter("timestamp");/// 时间戳
        String nonce = request.getParameter("nonce"); /// 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                logger.debug("wechat get success....");
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            out=null;
        }
    }*/
}
