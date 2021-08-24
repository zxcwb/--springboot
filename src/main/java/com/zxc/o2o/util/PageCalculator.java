package com.zxc.o2o.util;
//分页插件
public class PageCalculator {
   public static int calculateRowIndex(int pageIndex,int pageSize){
       return (pageIndex > 0)?(pageIndex-1)*pageSize:0;
   }
}
