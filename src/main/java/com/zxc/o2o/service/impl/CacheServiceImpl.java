package com.zxc.o2o.service.impl;

import com.zxc.o2o.cache.JedisUtil;
import com.zxc.o2o.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private JedisUtil.Keys jedisKeys;

    @Override
    public void removeFormCache(String keyPrefix) {
        Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
        for (String key : keySet){
            jedisKeys.del(key);
        }
    }
}
