package com.zxc.o2o.config.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zxc.o2o.util.DesUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.beans.PropertyVetoException;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/8 9:34
 * @Version 1.0
 *
 */
@Configuration
@MapperScan("com.zxc.o2o.dao")
public class DataSourceConfiguration {
    @Value(("${jdbc.driver}"))
    private String jdbcDriver;
    @Value(("${jdbc.url}"))
    private String jdbcUrl;
    @Value(("${jdbc.username}"))
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /*
    * spring-dao.xml 对应的datasource
    * */
    @Bean(name = "dataSource")
    public ComboPooledDataSource createDataSource(){
        //生成dataSource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //跟配置文件一样设置以下信息

        try {
            //驱动
            dataSource.setDriverClass(jdbcDriver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        //数据库连接URL
        dataSource.setJdbcUrl(jdbcUrl);
        //数据库用户名
        dataSource.setUser(DesUtil.getDecryptMessageStr(jdbcUsername));
        //数据库用户密码
        dataSource.setPassword(DesUtil.getDecryptMessageStr(jdbcPassword));

        //配置c3p0连接池的私有属性
        //连接池最大的线程数
        dataSource.setMaxPoolSize(30);
        //连接池最小的线程数
        dataSource.setMinPoolSize(10);
        //关闭 连接后不自动提交commit
        dataSource.setAutoCommitOnClose(false);
        //连接超时时间
        dataSource.setCheckoutTimeout(10000);
        //连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;
    }
}
