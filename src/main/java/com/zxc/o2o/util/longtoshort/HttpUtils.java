package com.zxc.o2o.util.longtoshort;

import com.alibaba.fastjson.JSONObject;
import com.zxc.o2o.util.wechat.MyX509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/13 12:34
 * @Version 1.0
 *
 */
public class HttpUtils {
    /**
     * HttpsUtil方法https请求结果返回蔚json类型
     * @param Url http请求地址
     * @param Method http请求类型支持POST GET
     * @param Output
     * @return InputStream转换成JSONObject后返回
     * @throws Exception
     */
    public static JSONObject httpsUtil(String Url, String Method, String Output) throws Exception{
        JSONObject jsonObject = null;
        URL conn_url =  new URL(Url);
        HttpURLConnection conn = (HttpURLConnection) conn_url.openConnection();
        conn.setRequestMethod(Method);
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.connect();
        //output获取access_token是不会用到
        if(Output != null){
            OutputStream outputstream =conn.getOutputStream();
            //字符集，防止出现中文乱码
            outputstream.write(Output.getBytes("UTF-8"));
            outputstream.close();
        }
        //正常返回代码为200
        if(conn.getResponseCode()==200){
            InputStream stream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            stream.close();
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        }
        System.out.println(conn.getResponseCode());
        return jsonObject;
    }

    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr)   {
        HttpsURLConnection httpUrlConn = null;
        OutputStream outputStream = null ;
        InputStream inputStream = null;

        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = { new MyX509TrustManager() };
        try{
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            jsonObject = JSONObject.parseObject(buffer.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            // 释放资源
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
            if (httpUrlConn != null ){
                httpUrlConn.disconnect();
            }
        }
        return jsonObject;
    }


}
