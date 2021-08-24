package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.AwardDao;
import com.zxc.o2o.dao.UserAwardMapDao;
import com.zxc.o2o.dao.UserShopMapDao;
import com.zxc.o2o.dto.UserAwardMapExecution;
import com.zxc.o2o.entity.UserAwardMap;
import com.zxc.o2o.entity.UserShopMap;
import com.zxc.o2o.enums.UserAwardMapStateEnum;
import com.zxc.o2o.exceptions.UserAwardMapOperationException;
import com.zxc.o2o.service.UserAwardMapService;
import com.zxc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 8:26
 * @Version 1.0
 *
 */
@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {

    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Autowired
    private UserShopMapDao userShopMapDao;

    @Autowired
    private AwardDao awardDao;

    @Override
    public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userAwardMapCondition != null && pageIndex != null && pageSize != null){
            //页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);

            List<UserAwardMap> userAwardMapList  = userAwardMapDao.queryUserAwardMapList(userAwardMapCondition,beginIndex,pageSize);
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardMapCondition);

            UserAwardMapExecution ue = new UserAwardMapExecution();
            ue.setUserAwardMapList(userAwardMapList);
            ue.setCount(count);
            return ue;
        }else {
            return null;
        }
    }

    @Override
    public UserAwardMap getUserAwardMapById(long userAwardId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardId);
    }

    @Override
    @Transactional
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) {
        //空值判断，主要确定userId和shopId不为空
        if (userAwardMap != null && userAwardMap.getShop() != null && userAwardMap.getUser().getUserId() != null
            && userAwardMap.getShop().getShopId() != null && userAwardMap.getUser() != null){
            //设置默认值
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            //从数据库里面获取award的完整信息设置进入userAwardMap
            //userAwardMap.setAward(awardDao.queryAwardByAwardById(userAwardMap.getAward().getAwardId()));
            try {
                int result = 0;
                //若该奖品需要消耗积分，则tb_user_shop_map对应的用户积分抵扣
                if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0){
                    //根据用户Id和店铺Id获取该用户在店铺的积分
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),userAwardMap.getShop().getShopId());
                    //判断该用户在店铺是否有积分
                    if (userShopMap !=null){
                        //如果有积分必须要确保大于本次兑换奖品所需要的积分数
                        if (userShopMap.getPoint() >= userAwardMap.getPoint()){
                            //积分抵扣
                            userShopMap.setPoint(userShopMap.getPoint()-userAwardMap.getPoint());
                            //更新积分信息
                            result = userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if (result <= 0){
                                throw new UserAwardMapOperationException("更新积分信息失败！");
                            }
                        }else {
                            throw new UserAwardMapOperationException("积分不足无法领取!");
                        }
                    }else {
                        throw new UserAwardMapOperationException("在本店铺没有积分，无法领取!");
                    }
                }

                //插入领取奖品的信息
                result = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (result <= 0){
                    throw new UserAwardMapOperationException("领取奖励失败！");
                }
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS);
            }catch (Exception e){
                throw new UserAwardMapOperationException("领取奖励失败:"+e.toString());
            }
        }else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_INFO);
        }
    }


    @Override
    @Transactional
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMapCondition) {
        if (userAwardMapCondition == null || userAwardMapCondition.getUserAwardId() == null || userAwardMapCondition.getUsedStatus() == 0){
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_ID);
        }else {
            try {
                //更新可用状态
                int  result = userAwardMapDao.updateUserAwardMap(userAwardMapCondition);
                if (result <= 0){
                    return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
                }else {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,userAwardMapCondition);
                }
            }catch (Exception e){
                throw new UserAwardMapOperationException("modifyUserAwardMap error:"+e.getMessage());
            }
        }
    }
}
