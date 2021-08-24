package com.zxc.o2o.service.impl;

import com.zxc.o2o.dao.AwardDao;
import com.zxc.o2o.dto.AwardExecution;
import com.zxc.o2o.dto.ImageHolder;
import com.zxc.o2o.entity.Award;
import com.zxc.o2o.enums.AwardStateEnum;
import com.zxc.o2o.exceptions.AwardOperationException;
import com.zxc.o2o.service.AwardService;
import com.zxc.o2o.util.FileUtil;
import com.zxc.o2o.util.ImageUtil;
import com.zxc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 10:20
 * @Version 1.0
 *
 */
@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardDao awardDao;


    @Override
    public AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize,String shopAdmin) {
        //将页码转换为行码
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        //依据查询条件，调用dao层返回相关的店铺列表
        List<Award> awardListPutOn = new ArrayList<>();
        List<Award> awardList = awardDao.queryAwardList(awardCondition,rowIndex,pageSize);
        //依据相同的查询条件，返回店铺总数
        int count = awardDao.queryAwardCount(awardCondition);
        AwardExecution se = new AwardExecution();
        if (awardList != null){
            if ("1".equals(shopAdmin)){
                se.setAwardList(awardList);
                se.setCount(count);
            }else {
                for (Award award : awardList){
                    if (award.getEnableStatus()==1){
                        awardListPutOn.add(award);
                    }
                }
                se.setAwardList(awardListPutOn);
                se.setCount(awardListPutOn.size());
            }
        }else {
            se.setState(AwardStateEnum.NULL_AWARD.getState());
        }
        return se;
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardByAwardById(awardId);
    }

    @Override
    @Transactional
    public AwardExecution addAward(Award award, ImageHolder thumbnail, List<ImageHolder> awardImgList) throws AwardOperationException {
        //空值判断
        if (award == null){
            return new AwardExecution(AwardStateEnum.NULL_AWARD);
        }
        try {
            //申请成功，给店铺信息赋初始值
            award.setEnableStatus(0);
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());

            //为了防止异常
            award.setAwardImg("");
            award.setPriority(1);
            //添加奖品信息
            int nums = awardDao.insertAward(award);
            if (nums <= 0){
                throw new AwardOperationException("奖品创建失败");
            }else {
                if (thumbnail.getImage() != null){
                    //存储图片
                    try {
                        addAwardImg(award,thumbnail);
                    }catch (Exception e){
                        throw new AwardOperationException("addawardImg Error："+e.getMessage());
                    }
                    //更新店铺的图片地址
                    nums = awardDao.updateAward(award);
                    if (nums <= 0){
                        throw new AwardOperationException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            throw new AwardOperationException("addaward error:"+e.getMessage());
        }
        return new AwardExecution(AwardStateEnum.CHECK,award);
    }

    @Override
    @Transactional
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail, List<ImageHolder> awardImgHolderList) throws AwardOperationException {
        if (award == null || award.getAwardId() == null){
            return new AwardExecution(AwardStateEnum.NULL_AWARD);
        }else {
            try {
                //1、判断是否需要处理图片，更新店铺信息
                if (thumbnail != null){
                    if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())){
                        Award tempAward = awardDao.queryAwardByAwardById(award.getAwardId());
                        if (tempAward.getAwardImg() != null){
                            ImageUtil.deleteFileOrPath(tempAward.getAwardImg());
                        }
                        addAwardImg(award,thumbnail);
                    }
                }
                //2、更新奖品信息
                award.setLastEditTime(new Date());
                int effectedNum = awardDao.updateAward(award);
                if (effectedNum <= 0){
                    return new AwardExecution(AwardStateEnum.INNER_ERROR);
                }else {
                    award = awardDao.queryAwardByAwardById(award.getAwardId());
                    return new AwardExecution(AwardStateEnum.SUCCESS,award);
                }
            }catch (Exception e){
                throw new AwardOperationException("modifyaward error："+e.getMessage());
            }
        }
    }

    private void addAwardImg(Award award, ImageHolder thumbnail) { //此处awardImg原本是File类型
        //获取award图片相对值路径
        String dest = FileUtil.getShopImagePath(award.getShopId());
        //CommonsMultipartFile awardImgs = new CommonsMultipartFile(fileItem);
        String awardImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        award.setAwardImg(awardImgAddr);
    }

}
