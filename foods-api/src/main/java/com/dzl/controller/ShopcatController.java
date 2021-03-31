package com.dzl.controller;

import com.dzl.pojo.bo.ShopcartBO;
import com.dzl.utils.DZLJSONResult;
import com.dzl.utils.JsonUtils;
import com.dzl.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@Api(value = "购物车接口controller",tags = "购物车接口相关得到api")
@RequestMapping("shopcart")
@RestController   /*默认返回出去的都是json对象*/
public class ShopcatController {


    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public DZLJSONResult add(
            @RequestParam String userId,//单个的请求参数
            @RequestBody ShopcartBO shopcartBO,//@RequestBody作为一个json对象
            HttpServletResponse response,
            HttpServletRequest request
            //涉及到cookie就会用到request和response
    )
    {

        if (StringUtils.isBlank(userId))
        {
            return DZLJSONResult.errorMsg("");
        }

        //  前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        // 需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
        //首先从redis缓存里面去取，看是否存在该商品
        String shopcartJson = redisOperator.get("shopcart:" + userId);
        List<ShopcartBO> shopcartList = null;
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopcartBO sc: shopcartList) {
                //通过商品的规格id去判断是否已经存在商品
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(shopcartBO.getSpecId())) {
                    //如果传进来的规格id和购物车中的规格id相等，则进行累加
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else {
            // redis中没有购物车
            shopcartList = new ArrayList<>();
            // 直接添加到购物车中
            shopcartList.add(shopcartBO);
        }


        // 覆盖现有redis中的购物车
        redisOperator.set("shopcart:" + userId, JsonUtils.objectToJson(shopcartList));

        return DZLJSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public DZLJSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return DZLJSONResult.errorMsg("参数不能为空");
        }

        //  用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除redis购物车中的商品
        String shopcartJson = redisOperator.get("shopcart:" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话则删除
            for (ShopcartBO sc: shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopcartList.remove(sc);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set("shopcart:" + userId, JsonUtils.objectToJson(shopcartList));
        }
        return DZLJSONResult.ok();
    }

}
