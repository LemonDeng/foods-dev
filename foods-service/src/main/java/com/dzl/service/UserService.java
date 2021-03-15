package com.dzl.service;

import com.dzl.pojo.Stu;
import com.dzl.pojo.Users;
import com.dzl.pojo.bo.UserBO;

public interface UserService {

    /*判断用户名是否存在*/
    public boolean queryUsernameIsExist(String username);

     /*前端传送到后端来接受的数据用BO接收*/
    /*创建用户*/
    public Users createUser(UserBO userBO);
    /*
    *检索用户名和密码是否匹配，用于登录
    * */
    public Users queryUserForLogin(String username,String password);


}
