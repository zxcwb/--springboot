package com.zxc.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

//实现mybatis的拦截器接口  这段注解配置很长，就是说拦截的请求类型 方法类型 传入的类对象
@Intercepts({@Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),
@Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);

     //u0020 = 空格 正则定义
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    //拦截器规则
    @Override
    public  Object intercept(Invocation invocation) throws Throwable {
        //是否同步处于活跃状态
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        //获取数据源参数
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        if (synchronizationActive != true){
            //如果是读方法
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)){
                //selectKey 为自增id查询主键（SELECT LAST_INSERT_ID()），使用主库
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                }else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    //此处正则不太理解
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]"," ");
                    //匹配正则表达式
                    if (sql.matches(REGEX)){
                        //写  增删改
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    }else {
                        //读
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        }else {
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        logger.debug("设置方法[{}] use [{}] Strategy,SqlCommand Type [{}]..",ms.getId(),lookupKey,
                ms.getSqlCommandType().name());
        //设置数据源类型
        DynamicDataSourceHolder.setDBType(lookupKey);
        return invocation.proceed();
    }

    //返回封装好的对象
    @Override
    public Object plugin(Object target) {
        //当我们拦截的类型是Executor，那么就拦截它
        if (target instanceof Executor){
            return Plugin.wrap(target,this);
        }else{
            return target;
        }
    }

    //类初始化设置
    @Override
    public void setProperties(Properties properties) {

    }
}
