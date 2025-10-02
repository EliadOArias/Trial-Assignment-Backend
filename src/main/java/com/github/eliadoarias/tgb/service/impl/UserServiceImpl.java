package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.*;
import com.github.eliadoarias.tgb.entity.Blacklist;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.BlacklistMapper;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.security.LoginUser;
import com.github.eliadoarias.tgb.service.UserService;
import com.github.eliadoarias.tgb.util.ImageStorageUtil;
import com.github.eliadoarias.tgb.util.JwtReader;
import com.github.eliadoarias.tgb.util.JwtUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Resource
    ImageStorageUtil imageStorageUtil;

    @Resource
    BlacklistMapper blacklistMapper;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UserInfo createUserInfo(User user){
        return new UserInfo(user.getUsername(), user.getName(), user.getUserId(), user.getUsertype(), imageStorageUtil.buildUrl(user.getAvatar()));
    }

    @Override
    public TokenInfo register(RegisterRequest dto) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if(user != null)throw new ApiException(ExceptionEnum.REGISTER_DUPLICATED);
        String userId = UUID.randomUUID().toString();
        User newUser = User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .name(dto.getName())
                .usertype(dto.getUsertype())
                .userId(userId)
                .avatar(Objects.isNull(
                        dto.getAvatar())?"avatar-default":imageStorageUtil.getRelativeUrl(dto.getAvatar())
                )
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
        return createUserInfo(user);
    }

    @Override
    public UserInfo viewByName(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) throw new ApiException(ExceptionEnum.USER_NOT_FOUND);
        return createUserInfo(user);
    }

    @Override
    public UserInfo update(UserUpdateRequest dto, String userId) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUserId, userId)
        );
        if(Objects.isNull(user)){throw new ApiException(ExceptionEnum.USER_NOT_FOUND);}
        if(!Objects.isNull(dto.getUsername())){
            User tryUser = baseMapper.selectOne(
                    new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())
            );
            if(!Objects.isNull(tryUser))throw new ApiException(ExceptionEnum.REGISTER_DUPLICATED);
            user.setUsername(dto.getUsername());
        }

        if(!Objects.isNull(dto.getName()))user.setName(dto.getName());
        if(!Objects.isNull(dto.getAvatar()))user.setAvatar(imageStorageUtil.getRelativeUrl(dto.getAvatar()));
        userMapper.updateById(user);
        return createUserInfo(user);
    }

    @Override
    public Object blacklistAdd(String username, String sourceUserId) {
        User sourceUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId, sourceUserId));
        User targetUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if(Objects.isNull(targetUser))
            throw new ApiException(ExceptionEnum.USER_NOT_FOUND);
        if(Objects.equals(sourceUser.getId(), targetUser.getId()))
            throw new ApiException(ExceptionEnum.BLACKLIST_DUPLICATED);
        Blacklist tryBlacklist = blacklistMapper.selectOne(
                new LambdaQueryWrapper<Blacklist>()
                        .eq(Blacklist::getSourceUserId,sourceUser.getId())
                        .eq(Blacklist::getTargetUserId,targetUser.getId())
        );
        if(!Objects.isNull(tryBlacklist)) throw new ApiException(ExceptionEnum.BLACKLIST_ADDED);
        Blacklist blacklist = Blacklist.builder()
                .sourceUserId(sourceUser.getId())
                .targetUserId(targetUser.getId())
                .build();
        blacklistMapper.insert(blacklist);
        return null;
    }

    @Override
    public Object blacklistDelete(String username, String sourceUserId) {
        User sourceUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId, sourceUserId));
        User targetUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        Blacklist blacklist = blacklistMapper.selectOne(
                new LambdaQueryWrapper<Blacklist>()
                        .eq(Blacklist::getSourceUserId, sourceUser.getId())
                        .eq(Blacklist::getTargetUserId, targetUser.getId())
        );
        if(Objects.isNull(blacklist)) throw new ApiException(ExceptionEnum.BLACKLIST_NOT_EXISTS);
        blacklistMapper.deleteById(blacklist.getId());
        return null;
    }

    @Override
    public BlacklistInfo blacklistGet(String userid) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId, userid));
        List<User> blockedUsers = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .exists(
                                "select 1 from blacklist b "+
                                        "where b.source_user_id = {0} "+
                                        "and b.target_user_id = user.id",
                                user.getId()
                        )
        );
        List<String> names = new ArrayList<>();
        for (User e:blockedUsers){
            names.add(e.getUsername());
        }
        return new BlacklistInfo(names);
    }

    @Override
    public TokenInfo refresh(String refreshToken) {
        JwtReader reader = jwtUtil.readToken(refreshToken);
        String userId = reader.getId();
        String accessToken = jwtUtil.generateAccessToken(userId);
        return new TokenInfo(accessToken,refreshToken);
    }
}




