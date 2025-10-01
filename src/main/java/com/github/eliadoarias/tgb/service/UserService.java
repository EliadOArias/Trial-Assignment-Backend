package com.github.eliadoarias.tgb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.eliadoarias.tgb.dto.*;
import com.github.eliadoarias.tgb.entity.User;

/**
* @author EArias
* @description 针对表【user】的数据库操作Service
* @createDate 2025-09-16 15:23:13
*/
public interface UserService extends IService<User> {
    TokenInfo register(RegisterRequest dto);
    TokenInfo login(String username, String password);
    UserInfo view(String userId);
    UserInfo viewByName(String username);
    UserInfo update(UserUpdateRequest dto, String userId);
    Object blacklistAdd(String username, String sourceUserId);
    Object blacklistDelete(String username, String sourceUserId);
    BlacklistInfo blacklistGet(String userid);
    TokenInfo refresh(String refreshToken);
}
