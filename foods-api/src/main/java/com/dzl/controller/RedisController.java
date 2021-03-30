package com.dzl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@RestController   /*默认返回出去的都是json对象*/
@ApiIgnore/*不会在接口文档显示的*/
@RequestMapping("redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

   // @Autowired
   // private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        //redisOperator.set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(String key) {
        return (String)redisTemplate.opsForValue().get(key);
       // return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object delete(String key) {
       redisTemplate.delete(key);
      //  redisOperator.del(key);
        return "OK";
    }
}
