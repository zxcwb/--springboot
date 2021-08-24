package com.zxc.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.o2o.cache.JedisUtil;
import com.zxc.o2o.dao.HeadLineDao;
import com.zxc.o2o.entity.HeadLine;
import com.zxc.o2o.exceptions.HeadLineOperationException;
import com.zxc.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;

    @Resource
    private JedisUtil.Keys jedisKeys;

    @Resource
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(HeadLineService.class);

    @Transactional
    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        //定义redis的key的前缀
        String key = HLLISTKEY;
        //定义接收对象
        List<HeadLine> headLineList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //拼接出redis的key
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null){
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        //判断key是否存在
        if (!jedisKeys.exists(key)){
            //若不存在，则从数据库中取出相应的数据
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            //将相关的实体类集合转换成string.存入redis里面对应的key中
            String jsonString;
            jsonString = mapper.writeValueAsString(headLineList);
            jedisStrings.set(key,jsonString);
        }else {
            //若存在，则直接从redis里面取出相应的数据
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = mapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }

    /*@Transactional
    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        return headLineDao.queryHeadLine(headLineCondition);
    }*/
}
