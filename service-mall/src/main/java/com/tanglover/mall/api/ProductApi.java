package com.tanglover.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.tanglover.mall.service.ProductService;
import com.tanglover.mall.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-07-16 14:58
 * @description:
 */
@RestController
public class ProductApi extends BaseApi {

    @Autowired
    ProductService productService;

    /**
     * @Author TangXu
     * @Description 增加库存
     * @Date 2019/7/19 14:44
     * @Param [request]
     */
    @RequestMapping("/increaseStock")
    public Map<String, Object> increaseStock(HttpServletRequest request) {
        JSONObject reqJson = HttpUtils.getJSONObject(request);
        if (null == reqJson) {
            return returnError(10001, "参数错误");
        }
        long userId = reqJson.getLongValue("userId");
        if (0 == userId) {
            return returnError(10001, "参数错误");
        }
        long productId = reqJson.getLongValue("productId");
        if (0 == productId) {
            return returnError(10001, "参数错误");
        }
        long stock = reqJson.getLongValue("stock");
        return returnSuccess(productService.increaseStock(productId, stock));
    }

    /**
     * @Author TangXu
     * @Description 增加库存
     * @Date 2019/7/19 14:44
     * @Param [request]
     */
    @RequestMapping("/reduceStock")
    public Map<String, Object> reduceStock(HttpServletRequest request) {
        JSONObject reqJson = HttpUtils.getJSONObject(request);
        if (null == reqJson) {
            return returnError(10001, "参数错误");
        }
        long userId = reqJson.getLongValue("userId");
        if (0 == userId) {
            return returnError(10001, "参数错误");
        }
        long productId = reqJson.getLongValue("productId");
        if (0 == productId) {
            return returnError(10001, "参数错误");
        }
        long stock = reqJson.getLongValue("stock");
        return returnSuccess(productService.reduceStock(productId, stock));
    }

    /**
     * @Author TangXu
     * @Description 获取产品列表
     * @Date 2019/7/23 17:53
     * @Param [request]
     */
    @RequestMapping("/products")
    public Map<String, Object> products(HttpServletRequest request) {
        JSONObject reqJson = HttpUtils.getJSONObject(request);
        if (null == reqJson) {
            return returnError(10001, "参数错误");
        }
        return returnSuccess(productService.products(reqJson));
    }
}