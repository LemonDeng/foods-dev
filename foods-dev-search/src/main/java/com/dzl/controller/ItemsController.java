package com.dzl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController   /*默认返回出去的都是json对象*/
public class ItemsController {
    /*代表一个get请求*/
    @GetMapping("/hello")
    public Object hello()
    {
        return "Hello Elasticsearch~";
    }
}
