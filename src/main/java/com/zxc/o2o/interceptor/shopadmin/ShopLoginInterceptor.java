package com.zxc.o2o.interceptor.shopadmin;

import com.zxc.o2o.entity.PersonInfo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/6 7:04
 * @Version 1.0
 * 店家管理系统登录验证拦截器
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
    /*
    * 主要实现做事前拦截，即用户操作发生前，改写preHandler里的逻辑，进行拦截
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session里面取出用户信息出来
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null){
            //若用户的信息不为空，则将session里的用户信息转换成personInfo实体类对象
            PersonInfo user = (PersonInfo) userObj;
            //做空值判断，确保userId不为空并且账号的可用状态为1，并且用户类型为店家
            if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1){
                //若通过验证则返回true，用户进行接下来的操作正常执行
                return true;
            }
        }
        //若不满足条件登录，则直接跳转到用户登录的页面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('http://zxcservice.xyz"+request.getContextPath()
                + "/local/login?userType=2','_self')");
        out.println("</script>");
        out.println("</html>");
        out.flush();
        out.close();
        //不符合条件的给出提示信息，并转发到登录页面
       /*request.setAttribute("errMsg", "您还没有登录，请先登录！");
        //请求转发
        request.getRequestDispatcher("/WEB-INF/html/local/login.html").forward(request, response);
        response.sendRedirect("/o2o/local/login");*/
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
