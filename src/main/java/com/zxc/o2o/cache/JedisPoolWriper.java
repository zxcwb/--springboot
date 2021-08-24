package com.zxc.o2o.cache;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//创建redis连接池

public class JedisPoolWriper {

    //redis连接对象
    private JedisPool jedisPool;

    public JedisPoolWriper(final JedisPoolConfig poolConfig,final String hostname,final int port){
        try {
            jedisPool = new JedisPool(poolConfig,hostname,port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //获取redis连接池对象
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    //注入redis连接对象
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
