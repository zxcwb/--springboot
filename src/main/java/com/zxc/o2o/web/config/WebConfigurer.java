package com.zxc.o2o.web.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@Configuration
public class WebConfigurer extends WebMvcConfigurationSupport {

    //默认是Linux系统
    String upload = "/opt/static";

    @Override
    protected void  addResourceHandlers(ResourceHandlerRegistry registry) {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")){
            //说明是windows系统
            upload = "D:/images/";
        }
        //最后把img下的目录映射到指定的路径下
        registry.addResourceHandler("/images/**").addResourceLocations("file:///"+upload);
        System.out.println("我拦截了===========================================================");
        super.addResourceHandlers(registry);
    }
}
