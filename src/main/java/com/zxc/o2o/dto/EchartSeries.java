package com.zxc.o2o.dto;

import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/14 7:52
 * @Version 1.0
 *
 */
public class EchartSeries {
    private String name;
    private String type = "bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
