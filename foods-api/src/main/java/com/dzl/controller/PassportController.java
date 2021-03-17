package com.dzl.controller;

import com.dzl.pojo.Users;
import com.dzl.pojo.bo.UserBO;
import com.dzl.service.UserService;
import com.dzl.utils.CookieUtils;
import com.dzl.utils.DZLJSONResult;
import com.dzl.utils.JsonUtils;
import com.dzl.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@Api(value = "注册登录",tags = "用于注册登录的相关接口")
@RestController   /*默认返回出去的都是json对象*/
@RequestMapping("passport")/*路由*/
public class PassportController {
    /*代表一个get请求*/
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名已经存在",notes = "用户名已经存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    /*RequestParam一种请求类的参数*/
    public DZLJSONResult usernameIsExist(@RequestParam String username) {
        /*判断用户名不能为空*/
        if (StringUtils.isBlank(username)) {
            return DZLJSONResult.errorMsg("用户名不能为空");
        }
        /*查找注册的用户名是否存在*/
        boolean isExist = userService.queryUsernameIsExist(username);

        if (isExist) {
            return DZLJSONResult.errorMsg("用户名已经存在");
        }

        /*请求成功，用户名没有重复*/
        return DZLJSONResult.ok();
    }

    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public DZLJSONResult regist(@RequestBody UserBO userBO,
                                HttpServletResponse response,
                                HttpServletRequest request) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        //0.判断用户名和密码必须不为空

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPwd)
        ) {
            return DZLJSONResult.errorMsg("用户名或密码不能为空");
        }

        //1.查询用户是否存在
        boolean isExist = userService.queryUsernameIsExist(username);

        if (isExist) {
            return DZLJSONResult.errorMsg("用户名已经存在");
        }

        //2.密码长度不能少于6位
        if (password.length() < 6) {
            return DZLJSONResult.errorMsg("密码长度不能小于6位");
        }

        //3.判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return DZLJSONResult.errorMsg("两次输入的密码不一致");
        }

        //4.实现注册
        Users userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request,response,"user",JsonUtils.objectToJson(userResult));
        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据
        return DZLJSONResult.ok();

    }
    /*通过cookie来实现用户信息的显示*/
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public DZLJSONResult login(@RequestBody UserBO userBO,
                               HttpServletRequest request,//request存储了客户端的请求信息
                               HttpServletResponse response) //response存储了服务器端的想赢信息
            throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();


        //0.判断用户名和密码必须不为空

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)
        ) {
            return DZLJSONResult.errorMsg("用户名或密码不能为空");
        }

        //4.实现登录
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null)
        {
            return DZLJSONResult.errorMsg("用户名或密码不正确");
        }

         userResult = setNullProperty(userResult);

        /*Cookie里面封装了用户信息*/
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);


        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据


        return DZLJSONResult.ok(userResult);

    }

    private Users setNullProperty(Users userResult)
    {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public DZLJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

        return DZLJSONResult.ok();
    }
}
