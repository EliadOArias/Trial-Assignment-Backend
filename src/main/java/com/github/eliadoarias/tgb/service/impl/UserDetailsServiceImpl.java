package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.security.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws ApiException {
        //查询账号
        log.info("Loading UserDetails for {}", username);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new ApiException(ExceptionEnum.LOGIN_ERROR);
        }
        //产生user
        return new LoginUser(user);
    }
}
