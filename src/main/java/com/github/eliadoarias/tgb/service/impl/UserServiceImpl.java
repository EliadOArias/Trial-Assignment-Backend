package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.TokenInfo;
import com.github.eliadoarias.tgb.dto.UserInfo;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.security.LoginUser;
import com.github.eliadoarias.tgb.service.UserService;
import com.github.eliadoarias.tgb.util.JwtUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Resource
    PasswordEncoder encoder;

    @Resource
    UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public TokenInfo register(String username, String password, String name, Integer usertype) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(queryWrapper);
        if(user != null){
            throw new ApiException(ExceptionEnum.REGISTER_DUPLICATED);
        }
        String userId = UUID.randomUUID().toString();
        User newUser = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .name(name)
                .usertype(usertype)
                .userId(userId)
                .build();
        baseMapper.insert(newUser);
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId, 60 * 60 * 1000);
        return new TokenInfo(accessToken,refreshToken);
    }

    @Override
    public TokenInfo login(String username, String password) {
        //封装
        UsernamePasswordAuthenticationToken auToken = new UsernamePasswordAuthenticationToken(username, password);
        //授权校验
        Authentication authentication = authenticationManager.authenticate(auToken);
        if(authentication == null){
            throw new ApiException(ExceptionEnum.LOGIN_ERROR);
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getUserId();
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId, 60 * 60 * 1000);
        return new TokenInfo(accessToken,refreshToken);
    }

    @Override
    public UserInfo view(String userId) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId, userId));
        if (user == null){
            throw new ApiException(ExceptionEnum.NOT_FOUND);
        }
        return new UserInfo(user.getUsername(), user.getName(), user.getUserId(), user.getUsertype());
    }

    @Override
    public UserInfo viewByName(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null){
            throw new ApiException(ExceptionEnum.NOT_FOUND);
        }
        return new UserInfo(user.getUsername(), user.getName(), user.getUserId(), user.getUsertype());
    }
}




