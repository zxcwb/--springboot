package com.zxc.o2o.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class ImageHolder {
    private String imageName;
    private InputStream image;

    public ImageHolder(String imageName, InputStream image) {
        this.imageName = imageName;
        this.image = image;
    }

    public ImageHolder() {
    }
}
