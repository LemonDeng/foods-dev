package com.dzl.controller.center;

import com.dzl.controller.BaseController;
import com.dzl.pojo.Users;
import com.dzl.pojo.bo.center.CenterUserBO;
//import com.dzl.resource.FileUpload;
import com.dzl.pojo.vo.UsersVO;
import com.dzl.resource.FileUpload;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public DZLJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,//加个@Valid去验证后面的BO
            BindingResult result,//所有验证我的错误信息都会存在这个result里面
            HttpServletRequest request, HttpServletResponse response) {

        //判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return DZLJSONResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        UsersVO usersVO = conventUsersVO(userResult);

        // userResult = setNullProperty(userResult);
        //把更新之后的用户信息更新到cookie里面
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);

        //  后续要改，增加令牌token，会整合进redis，分布式会话

        return DZLJSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        //获取错误的所有属性存入List集合
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发生验证错误所对应的某一个属性
            String errorField = error.getField();
            //验证的错误信息
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
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


    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public DZLJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
                    String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletResponse response,
            HttpServletRequest request) {
        //首先定义头像保存的地址
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为每一个用户增加一个userId,用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        //开始文件上传
        if (file != null)
        {
            FileOutputStream fileOutputStream = null;
            //获得文件上传的文件名称
            //Ctrl+Alt+t对代码块进行异常处理
            try {
                String fileName = file.getOriginalFilename();

                if (StringUtils.isNotBlank(fileName))
                {
                    // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
                    String fileNameArr[] = fileName.split("\\.");

                    //获取文件的后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    //对上传文件后缀的判断
                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg"))
                    {
                        DZLJSONResult.errorMsg("图片格式不正确");
                    }
                    // face-{userid}.png
                    // 文件名称重组 覆盖式上传，增量式：额外拼接当前时间
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    Date date = new Date();
                    String format = simpleDateFormat.format(date);
                    String newFileName = "face-" + userId +"-"+ format +"." + suffix;


                    // 上传的头像最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                    // 用于提供给web服务访问的地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);
                    //判断传进来的路径是否存在
                    if (outFile.getParentFile() != null)
                    {
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    //文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return DZLJSONResult.errorMsg("文件不能为空！");
        }

        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        //userResult = setNullProperty(userResult);
        UsersVO usersVO = conventUsersVO(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return DZLJSONResult.ok();
    }



}
