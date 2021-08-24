package com.zxc.o2o.dto;

import lombok.Data;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 17:31
 * @Version 1.0
 * 用来接收平台二维码的信息
 */
@Data
public class WechatInfo {
    private Long customerId;
    private Long productId;
    private Long userAwardId;
    private Long createTime;
    private Long shopId;
}
