package com.dzl.controller.center;

import com.dzl.controller.BaseController;
import com.dzl.pojo.Users;
import com.dzl.pojo.bo.center.CenterUserBO;
//import com.dzl.resource.FileUpload;
import com.dzl.service.center.CenterUserService;
import com.dzl.utils.CookieUtils;
import com.dzl.utils.DZLJSONResult;
import com.dzl.utils.DateUtil;
import com.dzl.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public DZLJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,//加个@Valid去验证后面的BO
            BindingResult result,//所有验证我的错误信息都会存在这个result里面
            HttpServletRequest request, HttpServletResponse response) {

        //判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors())
        {
            Map<String,String> errorMap = getErrors(result);
            return DZLJSONResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        userResult = setNullProperty(userResult);
        //把更新之后的用户信息更新到cookie里面
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

         // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

            return DZLJSONResult.ok();
    }

    private Map<String,String> getErrors(BindingResult result)
    {
        Map<String,String> map = new HashMap<>();
        //获取错误的所有属性存入List集合
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList)
        {
            //发生验证错误所对应的某一个属性
            String errorField = error.getField();
            //验证的错误信息
            String errorMsg = error.getDefaultMessage();
            map.put(errorField,errorMsg);
        }
        return map;

    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
