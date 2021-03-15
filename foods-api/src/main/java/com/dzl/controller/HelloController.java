package com.dzl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@RestController   /*默认返回出去的都是json对象*/
@ApiIgnore/*不会在接口文档显示的*/
public class HelloController {
    /*代表一个get请求*/
    @GetMapping("/hello")
    public Object hello()
    {
        return "HelloWolrd";
    }
}
