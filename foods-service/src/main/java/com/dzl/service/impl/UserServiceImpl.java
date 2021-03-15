package com.dzl.service.impl;

import com.dzl.enums.Sex;
import com.dzl.mapper.StuMapper;
import com.dzl.mapper.UsersMapper;
import com.dzl.pojo.Stu;
import com.dzl.pojo.Users;
import com.dzl.pojo.bo.UserBO;
import com.dzl.service.StuService;
import com.dzl.service.UserService;
import com.dzl.utils.DateUtil;
import com.dzl.utils.MD5Utils;
import org.apache.tomcat.jni.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/*Service是一个容器需要被扫描到，所以要添加注解Service*/
@Service
public class UserServiceImpl implements UserService {
    /*利用Autowired注解来注入Mapper*/
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        //Example通过条件去进行查询的
        Example userExample = new Example(Users.class);//对Users这个实体类进行查询

        Example.Criteria userCriteria = userExample.createCriteria();
        
        userCriteria.andEqualTo("username",username);//判断传进来的参数对比是否一致

        Users result = usersMapper.selectOneByExample(userExample);

        return result == null? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)/*事物*/
    @Override
    public Users createUser(UserBO userBO) {

        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        //设置默认的头像
        user.setFace(USER_FACE);
        //默认的生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //默认性别为保密
        user.setSex(Sex.secret.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        //Example通过条件去进行查询的
        Example userExample = new Example(Users.class);//对Users这个实体类进行查询

        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("username",username);//判断传进来的参数对比是否一致
        userCriteria.andEqualTo("password",password);//判断传进来的参数对比是否一致


        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }
}
