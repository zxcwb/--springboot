package com.zxc.o2o.service;

public interface CacheService {
    //根据key的前缀删除匹配该模式下的所有key-value
    void removeFormCache(String keyPrefix);
}
