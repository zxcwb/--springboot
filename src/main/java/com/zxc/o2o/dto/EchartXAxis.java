package com.zxc.o2o.dto;

import java.util.HashSet;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 7:51
 * @Version 1.0
 *
 */
public class EchartXAxis {
    private String type="category";
    //为了去重
    private HashSet<String> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashSet<String> getData() {
        return data;
    }

    public void setData(HashSet<String> data) {
        this.data = data;
    }
}
