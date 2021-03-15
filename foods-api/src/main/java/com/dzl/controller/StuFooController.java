package com.dzl.controller;

import com.dzl.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@RestController   /*默认返回出去的都是json对象*/
@ApiIgnore
public class StuFooController {

    @Autowired
    private StuService stuService;

    /*代表一个get请求*/
    @GetMapping("/getStu")
    public Object getStu(int  id)
    {
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu( )
    {
         stuService.saveStu();
         return "Ok";
    }

    @PostMapping("/updateStu")
    public Object updateStu(int id )
    {
        stuService.updateStu(id);
        return "Ok";
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(int id )
    {
        stuService.deleteStu(id);
        return "Ok";
    }
}
