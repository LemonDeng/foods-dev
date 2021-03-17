package com.dzl.controller;

import com.dzl.pojo.bo.ShopcartBO;
import com.dzl.utils.DZLJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@Api(value = "购物车接口controller",tags = "购物车接口相关得到api")
@RequestMapping("shopcart")
@RestController   /*默认返回出去的都是json对象*/
public class ShopcatController {

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
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return DZLJSONResult.ok();
    }


}
