package com.zxc.o2o.dao;

import com.zxc.o2o.entity.Award;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 7:55
 * @Version 1.0
 *  奖品Dao
 */
public interface AwardDao {
    /*
    * 依据传入进来的查询条件分页显示奖品信息列表
    * */
    List<Award> queryAwardList(@Param("awardCondition")Award awardCondition,
                               @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    /*
    * 配合queryAwardList返回相同查询条件下的奖品数
    * */
    int queryAwardCount(@Param("awardCondition")Award awardCondition);

    /*
    * 通过awardId查询奖品信息
    * */
    Award queryAwardByAwardById(long awardId);

    /*
    * 添加奖品信息
    * */
    int insertAward(Award award);


    /*
    * 更新奖品信息
    * */
    int updateAward(Award award);

    /*
    * 删除奖品信息
    * */
    int deleteAward(@Param("awardId") long awardId,@Param("shopId")long shopId);

}
