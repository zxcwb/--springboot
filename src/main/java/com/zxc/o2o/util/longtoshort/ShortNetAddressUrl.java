package com.zxc.o2o.util.longtoshort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 10:49
 * @Version 1.0
 *
 */
@Slf4j
public class ShortNetAddressUrl {
    public static int TIMEOUT = 30*100;
    public static String ENCODING = "UTF-8";

    /*
    * 根据传入的url，通过访问百度短视频的接口，将其转换成端的URL
    * */
    public static String getShortURL(String originURL){
       String tinyUrl = null;
       try {
           //指定百度短视频的的接口
           URL url = new URL("http://dwz.cn/create.php");
           //建立连接
           HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
           //使用连接进行输出
           httpURLConnection.setDoInput(true);
           //使用连接进行输入
           httpURLConnection.setDoInput(true);
           //不使用缓存
           httpURLConnection.setUseCaches(false);
           //设置连接超时时间为30S
           httpURLConnection.setConnectTimeout(TIMEOUT);
           //设置请求模式为POST
           httpURLConnection.setRequestMethod("POST");
           //设置POST信息，这里为传入的原始URL
           String postData = URLEncoder.encode(originURL.toString(),"utf-8");
           //输出原始的url
           httpURLConnection.getOutputStream().write(("url="+postData).getBytes());
           //连接百度短视频接口
           httpURLConnection.connect();
           //获取返回的字符串
           String responseStr = getResponseStr(httpURLConnection);
           log.info("response string:"+responseStr);
           //在字符串里获取tinyurl，即短视频连接
           tinyUrl = getValueByKey(responseStr,"tinyurl");
           log.info("tinyurl:"+tinyUrl);
           //关闭连接
           httpURLConnection.disconnect();
       }catch (Exception e){
           e.printStackTrace();
           log.error("getshortURL error:"+e.toString());
       }
       return tinyUrl;
    }


    /*
    * 通过HttpConnection获取返回的字符串
    * */
    private static String getResponseStr(HttpURLConnection httpURLConnection) throws IOException {
        StringBuffer result = new StringBuffer();
        //从连接中获取http状态码
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK){
            //如果返回的是200
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,ENCODING));
            String inputLine = "";
            while ((inputLine = reader.readLine())!= null){
                //将消息逐行读入结果中
                result.append(inputLine);
            }
        }
        //将结果转换成String并返回
        return String.valueOf(result);
    }


    /*
     * JSON 传入的key获取value
     * */
    private static String getValueByKey(String responseStr, String key) {
        ObjectMapper mapper = new ObjectMapper();
        //定义json节点
        JsonNode node;
        String targetValue = null;

        try {
            //把调用返回的消息串转换成json对象
            node = mapper.readTree(responseStr);
            //根据key从json对象里获取对应的值
            targetValue = node.get(key).asText();
        }catch (JsonProcessingException e){
            log.error("getValueByKey error:"+e.toString());
        }catch (IOException e){
            log.error("getValueByKey error:"+e.toString());
        }
        return targetValue;
    }

    /*
    * 百度短链接接口
    * */
    public static void main(String[] args){
        String url = getShortURL("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");
        System.out.println(url);
    }
}
