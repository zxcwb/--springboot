package com.zxc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
   //需要加密的字段数组
    private  String[] encryptPropNames = {"jdbc.username","jdbc.password"};

    //对关键的属性进行转换
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)){
            //对已加密的字段进行解密工作
            String decryptValue = DesUtil.getDecryptMessageStr(propertyValue);
            return decryptValue;
        }else {
            return propertyValue;
        }
    }

    //该属性是否加密
    private boolean isEncryptProp(String propertyName) {
        //若等于需要加密的属性，则进行加密
        for (String encryptPropertyName : encryptPropNames){
            if (encryptPropertyName.equals(propertyName)){
                return true;
            }
        }
        return false;
    }

}
