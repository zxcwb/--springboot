package com.zxc.o2o.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/2 9:10
 * @Version 1.0
 *
 */
public class GetHttpStatusCode {
    private static void httpClientPost() {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.poorren.com");
        try {
            ContentProducer cp = new ContentProducer() {
                public void writeTo(OutputStream outstream) throws IOException {
                    Writer writer = new OutputStreamWriter(outstream, "UTF-8");
                    writer.write("");
                    writer.flush();
                }
            };
            post.setEntity(new EntityTemplate(cp));
            HttpResponse response = client.execute(post);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
