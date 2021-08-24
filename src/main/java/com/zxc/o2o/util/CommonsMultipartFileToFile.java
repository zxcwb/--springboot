package com.zxc.o2o.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommonsMultipartFileToFile {
    //将InputStream类型转化为File类型
    private static void inputStreamToFile(InputStream ins, File file) {
        //将输出流转化为文件即可
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }catch(Exception e) {
            throw new RuntimeException("调用inputStreamToFile产生异常："+e.getMessage());
        }finally {
            try {
                if(os != null) {
                    os.close();
                }
                if(ins != null) {
                    ins.close();
                }
            }catch(IOException e) {
                throw new RuntimeException("inputStreamToFile关闭io时产生异常："+e.getMessage());
            }
        }
    }
}
