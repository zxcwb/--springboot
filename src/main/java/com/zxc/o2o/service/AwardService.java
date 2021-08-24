package com.zxc.o2o.service;

import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.dto.AwardExecution;
import com.zxc.o2o.entity.Award;

import com.zxc.o2o.exceptions.AwardOperationException;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 17:31
 * @Version 1.0
 *
 */
public interface AwardService {
    /*
    * 根据传入的条件分页返回奖品列表，并返回该查询条件下的总数
    * */
    AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize,String shopAdmin);

    /*
    * 根据awardId查询奖品信息
    * */
    Award getAwardById(long awardId);

    /*
    * 添加奖品信息，并添加奖品图片
    * */
    AwardExecution addAward(Award award, ImageHolder thumbnail,
                                List<ImageHolder> awardImgList)throws AwardOperationException;

    /*
    * 根据传入的奖品实例修改对应的奖品信息，若传入图片则替换掉原先的图片
    * */
    AwardExecution modifyAward(Award award, ImageHolder thumbnail, List<ImageHolder> awardImgHolderList) throws AwardOperationException;
    
}


