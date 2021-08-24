package com.zxc.o2o.web.local;

import com.zxc.o2o.dto.LocalAuthExecution;
import com.zxc.o2o.entity.LocalAuth;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.enums.LocalAuthStateEnum;
import com.zxc.o2o.service.LocalAuthService;
import com.zxc.o2o.util.CodeUtil;
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
 * @Date: 2021/8/3 16:04
 * @Version 1.0
 *
 */
@Controller
@RequestMapping(value = "local",method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    @RequestMapping(value = "/bindlocalauth",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> bindLocalAuth(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        //获取输入的账号
        String username = HttpServletRequestUtil.getString(request,"username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request,"password");
        //从session中获取当前用户的信息（用户通过微信登录之后，便能获取到用户的信息）
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        //非空判断,要求账号密码以及当前用户session非空
        if (username != null && password != null && personInfo != null && personInfo.getUserId() != null){
            //创建LocalAuth对象并赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUserName(username);
            localAuth.setPassword(password);
            localAuth.setUserId(personInfo.getUserId());
            localAuth.setPersonInfo(personInfo);
            //绑定账号
            LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
            if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",localAuthExecution.getStateInfo());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名密码均不能为空");
        }
        return modelMap;
    }

    @RequestMapping(value = "changelocalpwd",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> changeLocalPwd(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        //获取输入的账号
        String username = HttpServletRequestUtil.getString(request,"username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request,"password");
        //获取新密码
        String newPassword = HttpServletRequestUtil.getString(request,"newPassword");
        //从Session中获取当前用户信息
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        if (username != null && password != null && newPassword != null &&
                personInfo != null && personInfo.getUserId() != null&&!password.equals(newPassword)) {
            try {
                //查看原先账号，看看与输入的账号是否一致，不一致的则认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
                if (localAuth == null || !localAuth.getUserName().equals(username)) {
                    //不一致则退出
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的账号非本次登录的账号");
                    return modelMap;
                }

                //修改平台账号的用户密码
                LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(personInfo.getUserId(), username, password, newPassword);
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", localAuthExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入密码");
        }
        return modelMap;
    }

    @RequestMapping(value = "/logincheck",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> loginCheck(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取是否需要进行验证码校验的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request,"needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        //获取输入的账号
        String username = HttpServletRequestUtil.getString(request,"username");
        //获取输入密码
        String password = HttpServletRequestUtil.getString(request,"password");
        //非空校验
        if (username != null && password != null){
            //传入账号和密码去获取平台账号信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPassword(username,password);
            if (localAuth != null){
                //登录成功
                if (localAuth.getUserType() == 1){
                    modelMap.put("success",true);
                    modelMap.put("userType",1);
                }else if (localAuth.getUserType() == 2){
                    modelMap.put("success",true);
                    modelMap.put("userType",2);
                }
                //在Session存入用户信息
                PersonInfo personInfo = localAuth.getPersonInfo();
                request.getSession().setAttribute("user",personInfo);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误");
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名和密码均不能为空");
        }
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    private Map<String,Object> logout(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user",null);
        modelMap.put("success",true);
        return modelMap;
    }
}
