package com.github.eliadoarias.tgb.service;

import com.github.eliadoarias.tgb.dto.LoginResponse;
import com.github.eliadoarias.tgb.dto.UserInfo;
import com.github.eliadoarias.tgb.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.eliadoarias.tgb.result.AjaxResult;

/**
* @author EArias
* @description 针对表【user】的数据库操作Service
* @createDate 2025-09-16 15:23:13
*/
public interface UserService extends IService<User> {
    LoginResponse register(String username, String password, String name, Integer usertype);
    LoginResponse login(String username, String password);
    UserInfo viewMe(User userId);
}
