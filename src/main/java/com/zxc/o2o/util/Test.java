package com.zxc.o2o.util;

import com.zxc.o2o.util.wechat.WechatUserUtil;

import java.io.IOException;
import java.util.Properties;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/2 10:40
 * @Version 1.0
 *
 */
public class Test {
    public static void getUserAccessToken() throws IOException {
        Properties pro = new Properties();
        pro.load(WechatUserUtil.class.getClassLoader().getResourceAsStream(
                "wechat.properties"));
        String appId = DesUtil
                .getDecryptMessageStr(pro.getProperty("wechatappi"));

        String appsecret = DesUtil.getDecryptMessageStr(pro
                .getProperty("wechatappsecret"));

        System.out.println(appId);

    }

    /*public static void main(String[] args) throws IOException {
        Test.getUserAccessToken();
    }*/
}
