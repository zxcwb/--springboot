package com.zxc.o2o.config.redis;

import com.zxc.o2o.cache.JedisPoolWriper;
import com.zxc.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/8 15:30
 * @Version 1.0
 *
 */
@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value(("${redis.pool.maxActive}"))
    private int maxActive;
    @Value(("${redis.pool.maxIdle}"))
    private int maxIdle;
    @Value(("${redis.pool.MaxWaitMillis}"))
    private long maxWaitMillis;
    @Value(("${redis.pool.testOnBorrow}"))
    private boolean testOnBorrow;


    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisPoolWriper;
    @Autowired
    private JedisUtil jedisUtil;


    /*
    * 创建redis连接池配置
    * */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可以分配多个jedis实例
        jedisPoolConfig.setMaxTotal(maxActive);
        //连接池中最多可空闲maxIdle个连接，这里取值为20,
        jedisPoolConfig.setMaxIdle(maxIdle);
        //设置最大的等待时间，当没有可用连接时，连接池等待连接被归还的最大时间，超过时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /*
    * 创建redis连接池，并做相关的配置
    * */
    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper(){
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig,hostname,port);
        return jedisPoolWriper;
    }

    /*
    * 创建redis工具类，封装号redis的连接以及相关的操作
    * */
    @Bean(name = "jedisUtil")
    public JedisUtil createUtil(){
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    /*
    * redis的key操作
    * */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys(){
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys(jedisUtil);
        return jedisKeys;
    }

    /*
     * redis的strings操作
     * */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStirngs(){
        JedisUtil.Strings jedisStirngs = jedisUtil.new Strings(jedisUtil);
        return jedisStirngs;
    }

    /*
     * redis的key操作
     * */
    @Bean(name = "jedisLists")
    public JedisUtil.Lists createJedisLists(){
        JedisUtil.Lists jedisLists = jedisUtil.new Lists(jedisUtil);
        return jedisLists;
    }

    /*
     * redis的key操作
     * */
    @Bean(name = "jedisSets")
    public JedisUtil.Sets createJedisSets(){
        JedisUtil.Sets jedisSets = jedisUtil.new Sets(jedisUtil);
        return jedisSets;
    }

    /*
     * redis的key操作
     * */
    @Bean(name = "jedisHashs")
    public JedisUtil.Hash createJedisHashs(){
        JedisUtil.Hash jedisHashs = jedisUtil.new Hash(jedisUtil);
        return jedisHashs;
    }


}
