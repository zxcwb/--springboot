package com.zxc.o2o.util.longtoshort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.List;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 11:49
 * @Version 1.0
 *
 */
public class LongUrlToShortUrlUtil {

    /**
    * 百度
    */
    public static String convert1(String url) {
             CloseableHttpClient httpClient = HttpClients.createDefault();
             try {
                     HttpPost post = new HttpPost("http://www.dwz.cn/create.php");
                     List<NameValuePair> params = new LinkedList<NameValuePair>();
                     params.add(new BasicNameValuePair("url", url));
                     post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                     CloseableHttpResponse response = httpClient.execute(post);
                     String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
                     JSONObject object = JSON.parseObject(jsonStr);
                     return object.getString("tinyurl");
                 } catch (Exception e) {
                     e.printStackTrace();
                     return null;
                 }
            }
  /**
    * 新浪
    */
       public static String convert2(String url) {
           CloseableHttpClient httpClient = HttpClients.createDefault();
           try {
                   HttpPost post = new HttpPost("http://api.t.sina.com.cn/short_url/shorten.json");
                   List<NameValuePair> params = new LinkedList<NameValuePair>();
                   params.add(new BasicNameValuePair("url_long", url));
                   params.add(new BasicNameValuePair("source", "3271760578"));
                   post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                   CloseableHttpResponse response = httpClient.execute(post);
                   String json = EntityUtils.toString(response.getEntity(), "utf-8");
                   JSONArray jsonArray = JSONArray.parseArray(json);
                   JSONObject object = (JSONObject) jsonArray.get(0);
                   return object.getString("url_short");
               } catch (Exception e) {
                   e.printStackTrace();
                   return null;
               }
       }

    public static void main(String[] args) {
       String url =  convert1("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");
       System.out.println("url:"+url);
    }
}
