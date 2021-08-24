package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.UserProductMapDao;
import com.zxc.o2o.dao.UserShopMapDao;
import com.zxc.o2o.dto.UserProductMapExecution;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.UserProductMap;
import com.zxc.o2o.entity.UserShopMap;
import com.zxc.o2o.enums.UserProductMapStateEnum;
import com.zxc.o2o.exceptions.UserProductMapOperationException;
import com.zxc.o2o.service.UserProductMapService;
import com.zxc.o2o.util.PageCalculator;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 22:12
 * @Version 1.0
 *
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {

    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userProductCondition != null && pageIndex != null && pageSize != null){
            //页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            //依据查询条件分页取出列表
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition,beginIndex,pageSize);
            //按照同等条件查询条件总数
            int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
            UserProductMapExecution  se = new UserProductMapExecution();
            se.setUserProductMapList(userProductMapList);
            se.setCount(count);
            return se;
        }else {
            return null;
        }
    }

    /*
    * 添加消费记录
    * */
    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) {
        //空值判断
        if (userProductMap != null && userProductMap.getUser().getUserId() != null
        && userProductMap.getShop().getShopId() != null && userProductMap.getOperator().getUserId() != null){
            //设定默认值
            userProductMap.setCreateTime(new Date());
            try {
                //添加消费记录
                int result = userProductMapDao.insertUserProductMap(userProductMap);
                if (result <= 0){
                    throw new UserProductMapOperationException("添加消费记录失败!");
                }
                //若本次消费能够得到积分
                if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0){
                    //查询该顾客是否在店铺消费过
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),userProductMap.getProduct().getProductId());
                    if (userShopMap != null && userShopMap.getUserShopId() != null){
                        //消费过，需要进行加积分操作
                        userShopMap.setPoint(userShopMap.getPoint()+userProductMap.getPoint());
                        result = userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if (result <= 0){
                            throw new UserProductMapOperationException("更新积分信息失败！");
                        }
                    }else {
                        //在店铺没有消费记录，添加一条店铺积分信息（初始化会员一样）
                        userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),
                                userProductMap.getShop().getShopId(),userProductMap.getPoint());
                        result = userShopMapDao.insertUserShopMap(userShopMap);
                        if (result <= 0){
                            throw new UserProductMapOperationException("积分信息创建失败!");
                        }
                    }
                }
                return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS,userProductMap);
            }catch (Exception e){
                throw new UserProductMapOperationException("添加授权失败："+e.toString());
            }
        }else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    /*
    * 封装顾客积分信息
    * */
    private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
        UserShopMap userShopMap = null;
        if (userId != null && shopId != null){
            userShopMap = new UserShopMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(userId);
            userShopMap.setUser(customer);
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMap.setShop(shop);
            userShopMap.setCreateTime(new Date());
            userShopMap.setPoint(point);
        }
        return userShopMap;
    }
}
