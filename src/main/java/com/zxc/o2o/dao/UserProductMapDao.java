package com.zxc.o2o.dao;

import com.zxc.o2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/11 12:26
 * @Version 1.0
 * 顾客消费商品映射
 */
public interface UserProductMapDao {
   /*
   * 根据查询条件分页返回用户购买商品的记录列表
   * */
   List<UserProductMap> queryUserProductMapList(@Param("userProductCondition")UserProductMap userProductCondition,
                                                @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

   /*
   * 配合queryUserProductMapList根据相同的条件查询返回用户购买商品的记录总数
   * */
   int queryUserProductMapCount(@Param("userProductCondition")UserProductMap userProductCondition);

   /*
   * 添加一条用户购买商品的记录
   * */
   int insertUserProductMap(UserProductMap userProductMap);
}
