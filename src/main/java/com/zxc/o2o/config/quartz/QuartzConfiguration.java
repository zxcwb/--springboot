package com.zxc.o2o.config.quartz;


import com.zxc.o2o.service.ProductSellDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 20:41
 * @Version 1.0
 *
 */
@Configuration
public class QuartzConfiguration {

    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactory;
    @Autowired
    private CronTriggerFactoryBean productSellDailyTriggerFactory;

    /*
    * 创建jobDetail并返回
    * */
    @Bean(name = "jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail(){
        //new 出JobDetailFactory对象，此工厂主要用来制作一个jobDetail，即制作一个任务
        //由于我们所做的定时任务根本上讲就是执行一个方法，所以用这个工厂比较方便
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        //设置jobDetail的名字
        jobDetailFactoryBean.setName("product_sell_daily_job");
        //设置jobDetail的组名
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        //对于相同的JobDetail。当指定多个Trigger时，很可能第一个job完成以后，第二个就已经开始了
        //指定concurrent设置为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始
        jobDetailFactoryBean.setConcurrent(false);
        //指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        //指定运行任务的方法
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");
        return jobDetailFactoryBean;
    }

    /*
    * 创建cronTrigger并返回
    * */
    @Bean(name = "productSellDailyTriggerFactory")
    public CronTriggerFactoryBean cronTriggerFactoryBean(){
        //创建triggerFactory实例，用来创建trigger
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        //设置triggerFactory的名字
        triggerFactoryBean.setName("product_sell_daily_trigger");
        //设置triggerFactory的组名
        triggerFactoryBean.setGroup("job_product_sell_daily_group");
        //绑定jobDetail
        triggerFactoryBean.setJobDetail(jobDetailFactory.getObject());
        //设置cron表达式 凌晨跑一次
        triggerFactoryBean.setCronExpression("0 0 0 * * ? *");
        return triggerFactoryBean;
    }

    /*
    * 创建调度工厂并返回
    * */
    @Bean(name = "schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(productSellDailyTriggerFactory.getObject());
        return schedulerFactoryBean;
    }


}
