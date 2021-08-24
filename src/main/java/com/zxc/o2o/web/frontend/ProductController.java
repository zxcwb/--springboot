package com.zxc.o2o.web.frontend;

import com.zxc.o2o.dto.UserProductMapExecution;
import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.entity.Product;
import com.zxc.o2o.entity.Shop;
import com.zxc.o2o.entity.UserProductMap;
import com.zxc.o2o.service.UserProductMapService;
import com.zxc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPBinding;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/16 9:26
 * @Version 1.0
 *
 */

@Controller
@RequestMapping("frontend")
public class ProductController {

    @Autowired
    private UserProductMapService userProductMapService;

    /*
     * 列出某个顾客商品消费信息
     * */
    @RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByCustomer(HttpServletRequest request) {
        {
            Map<String, Object> modelMap = new HashMap<>();
            //获取分页信息
            int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
            int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

            //从session里获取顾客信息
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            //空值判断
            if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != -1) {
                UserProductMap userProductMapCondition = new UserProductMap();
                userProductMapCondition.setUser(user);
                long shopId = HttpServletRequestUtil.getLong(request, "shopId");
                if (shopId > -1) {
                    //若传入的店铺信息，则列出某个店铺下该顾客的消费历史
                    Shop shop = new Shop();
                    shop.setShopId(shopId);
                    userProductMapCondition.setShop(shop);
                }

                String productName = HttpServletRequestUtil.getString(request, "productName");
                if (productName != null) {
                    //若传入的商品名不为空，则按照商品名模糊查询
                    Product product = new Product();
                    product.setProductName(productName);
                    userProductMapCondition.setProduct(product);
                }

                //根据查询分页信息返回用户消费记录
                UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
                modelMap.put("userProductMapList", ue.getUserProductMapList());
                modelMap.put("count", ue.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
            }
            return modelMap;
        }
    }

}
