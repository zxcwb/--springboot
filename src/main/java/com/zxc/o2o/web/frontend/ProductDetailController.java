package com.zxc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.service.ProductService;
import com.zxc.o2o.util.CodeUtil;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ProductDetailController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/listproductdetailpageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long productId = HttpServletRequestUtil.getLong(request,"productId");
        Product product = null;
        if (productId > -1L){
            product = productService.getProductById(productId);

            //2.0新增
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user == null){
                modelMap.put("needQRCode",false);
            }else {
                modelMap.put("needQRCode",true);
            }

            modelMap.put("product",product);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty prodcutId");
        }
        return modelMap;
    }

    //微信获取用户信息的api前缀
    private static String urlPrefix;
    //微信获取用户api的中间部分
    private static String urlMiddle;
    //微信获取用户信息的api后缀
    private static String urlSuffix;
    //微信回传给的响应添加顾客商品映射的信息的url
    private static String productmapUrl;


    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }
    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }
    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }
    @Value("${wechat.productmap.url}")
    public void setProductmapUrl(String productmapUrl) {
        ProductDetailController.productmapUrl = productmapUrl;
    }

    /*
    * 生成商品的消费凭证,供操作员扫描，证明已经消费，微信扫一扫就能链接对应的商品
    * */
    @RequestMapping(value = "/generateqrcode4product",method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response){
        //获取前端传递过来的商品Id
        long productId = HttpServletRequestUtil.getLong(request,"productId");
        //从Session中获取当前顾客的信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if (productId != -1 && user != null && user.getUserId() != null){
            //获取当前时间戳，以保证二维码的时间的有效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            //将商品id，顾客Id和timeStamp传入content，赋值到state中，这样微信获取到这些信息后回传到用户商户
            //加上aaa是为了在添加信息的方法里替换到这些信息
            String content = "{aaaproductIdaaa:"+productId
                    +",aaacustomerIdaaa:"+user.getUserId()+",aaacreateTimeaaa:"+timeStamp+"}";
            try {
                //将content信息先进行编码以避免特殊字符造成的干扰，之后拼接出目标URL
                String loginUrl = urlPrefix + productmapUrl + urlMiddle + URLEncoder.encode(content,"UTF-8")+urlSuffix;
                //将目标URL转换成短的URL(暂时不使用)
                //调用二维码生成的工具类方法，生成二维码
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(loginUrl,response);
                //将二维码以文件流的方式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg,"png",response.getOutputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
