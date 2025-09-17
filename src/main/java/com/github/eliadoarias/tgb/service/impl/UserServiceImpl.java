package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.LoginResponse;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.UserService;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.util.JwtUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
* @author EArias
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-09-16 15:23:13
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    JwtUtil jwtUtil;

    @Override
    public LoginResponse register(String username, String password, String name, Integer usertype) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(queryWrapper);
        if(user != null){
            throw new ApiException(ExceptionEnum.REGISTER_DUPLICATED);
        }
        String userId = UUID.randomUUID().toString();
        User newUser = User.builder()
                .username(username)
                .password(password)
                .name(name)
                .usertype(usertype)
                .userId(userId)
                .build();
        baseMapper.insert(newUser);
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId, 60 * 60 * 1000);
        return new LoginResponse(accessToken,refreshToken);
    }

    @Override
    public LoginResponse login(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(queryWrapper);
        if(user != null){
            throw new ApiException(ExceptionEnum.REGISTER_DUPLICATED);
        }
        return null;
    }
}




