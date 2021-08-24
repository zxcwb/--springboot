package com.zxc.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.mchange.v2.lang.Coerce;
import com.zxc.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.zxc.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/9 9:37
 * @Version 1.0
 * 开启MVC。自动注入spring容器，WebMvcConfigurationAdapter：配置视图解析器
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类可以获得applicationContext
 */
@Configuration
@EnableWebMvc //等价于<mvc:annotation-driver  WebMvcConfigurerAdapter注意这里 WebMvcConfigurationSupport
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    //spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /*
    * 静态资源配置
    * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        //registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/upload/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/opt/static/image/o2o/upload/");
    }

    /*
     * 定义默认请求处理器
     * */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*
    * 创建视图解析器
    * */
    @Bean(name = "viewResolver ")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置spring容器
        viewResolver.setApplicationContext(applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /*
    * 创建文件上传解析器
    * */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.textproducer.char.string}")
    private String cString;
    @Value("${kaptcha.image.height}")
    private String height;
    @Value("${kaptcha.textproducer.font.size}")
    private String fsize;
    @Value("${kaptcha.noise.color}")
    private String nColor;
    @Value("${kaptcha.textproducer.char.length}")
    private String clength;
    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;


    //由于web.xml不生效了，这里需要配置kaptcha验证码Servlet
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servlet= new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha.border",border);//无边框
        servlet.addInitParameter("kaptcha.textproducer.font.color",fcolor);//字体颜色
        servlet.addInitParameter("kaptcha.image.width",width);//图片宽度
        servlet.addInitParameter("kaptcha.textproducer.char.string",cString);//使用哪些文字
        servlet.addInitParameter("kaptcha.image.height",height);//图片高度
        servlet.addInitParameter("kaptcha.textproducer.font.size",fsize);//字体大小
        servlet.addInitParameter("kaptcha.noise.color",nColor);//干扰线的颜色
        servlet.addInitParameter("kaptcha.textproducer.char.length",clength);//字符长度
        servlet.addInitParameter("kaptcha.textproducer.font.names",fnames);//字符
        return servlet;
    }

    /*
    * 添加拦截器配置
    * */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String interceptPath = "/shopadmin/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截的路径
        loginIR.addPathPatterns(interceptPath);
        //loginIR.addPathPatterns("/frontend/index");
        loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
        //还可以注册其他的拦截器
        InterceptorRegistration permission = registry.addInterceptor(new ShopPermissionInterceptor());
        permission.addPathPatterns(interceptPath);

        //配置不拦截的路径
        permission.excludePathPatterns("/shopadmin/getshoplist");
        permission.excludePathPatterns("/shopadmin/shoplist");
        permission.excludePathPatterns("/shopadmin/getshopinitinfo");
        permission.excludePathPatterns("/shopadmin/registershop");
        permission.excludePathPatterns("/shopadmin/getshopbyid");
        permission.excludePathPatterns("/shopadmin/modifyshop");
        permission.excludePathPatterns("/shopadmin/getshopmanagerinfo");
        permission.excludePathPatterns("/shopadmin/removeproductcategorys");
        permission.excludePathPatterns("/shopadmin/addproductcategorys");
        permission.excludePathPatterns("/shopadmin/getproductcategorylist");
        permission.excludePathPatterns("/shopadmin/modifyproduct");
        permission.excludePathPatterns("/shopadmin/getproductbyid");
        permission.excludePathPatterns("/shopadmin/addproduct");
        permission.excludePathPatterns("/shopadmin/getproductlistbyshop");
        permission.excludePathPatterns("/shopadmin/listshopauthmapsbyshop");
        permission.excludePathPatterns("/shopadmin/getshopauthmapbyid");
        permission.excludePathPatterns("/shopadmin/modifyshopauthmap");
        permission.excludePathPatterns("/shopadmin/addshopauthmap");
        permission.excludePathPatterns("/shopadmin/generateqrcode4shopauth");
        permission.excludePathPatterns("/shopadmin/listuserproductmapsbyshop");
        permission.excludePathPatterns("/shopadmin/listproductselldailyinfobyshop");
        permission.excludePathPatterns("/shopadmin/listusershopmapsbyshop");
        permission.excludePathPatterns("/shopadmin/listuserawardmapsbyshop");
        permission.excludePathPatterns("/shopadmin/listawardsbyshop");
        permission.excludePathPatterns("/shopadmin/getawardbyid");
        permission.excludePathPatterns("/shopadmin/modifyaward");
        permission.excludePathPatterns("/shopadmin/addaward");
    }
}
