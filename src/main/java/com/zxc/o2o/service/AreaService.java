package com.zxc.o2o.service;

import com.zxc.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    public static final String AREALISTKEY = "arealist";
    List<Area> getAreaList();
}
