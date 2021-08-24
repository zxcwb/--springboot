package com.zxc.o2o.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class DesUtil {
    private static Key key;
    //设置秘钥
    private static String KEY_STR = "myKey";
    private static String CHARSETNAME = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            //生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            //运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            //设置上秘钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            //初始化基于SHA1的算法对象
            generator.init(secureRandom);
            //生成秘钥对象
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取加密信息
    public static String getEncryptMessageStr(String str){
        //基于BASE64编码，接收byte[]并转换为String
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            //接收UTF-8编码
            byte[] bytes = str.getBytes(CHARSETNAME);
            //获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE,key);
            //加密
            byte[] doFinal = cipher.doFinal(bytes);
            //byte[] to encode好的String并返回
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //获取解密信息
    public static String getDecryptMessageStr(String str){
        //基于BASE64编码，接收byte[]并转换成String
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            //将字符串decode成byte[]
            byte[] bytes = base64Decoder.decodeBuffer(str);
            //获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE,key);
            //解密
            byte[] doFinal = cipher.doFinal(bytes);
            //返回解密之后的信息
            return new String(doFinal,CHARSETNAME);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
