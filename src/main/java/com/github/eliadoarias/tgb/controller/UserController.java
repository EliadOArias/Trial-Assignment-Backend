package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.config.JwtConfig;
import com.github.eliadoarias.tgb.dto.LoginRequest;
import com.github.eliadoarias.tgb.dto.TokenInfo;
import com.github.eliadoarias.tgb.dto.RegisterRequest;
import com.github.eliadoarias.tgb.dto.UserInfo;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户
 */

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private UserService userService;

    /**
     * 注册
     * 提供账号，密码，昵称，用户类型进行注册。注册后会自动生成一个外部id和内部id。
     * 前端需要存储两种token，并在大多数操作时于header中发送。
     * Authorization: Bearer xxxx-xxxx-xxxx
     * @param request 包含用户基本信息
     * @return 返回登录token
     */
    @PostMapping("/register")
    public AjaxResult<TokenInfo> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Try register: "+request.getUsername());
        return AjaxResult.success(userService.register(request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getUsertype()));
    }

    /**
     * 登录
     * @return 登录token
     */
    @PostMapping("/login")
    public AjaxResult<TokenInfo> login(@RequestBody LoginRequest request) {
        log.info("should never do here ");
        return null;
    }

    /**
     * 查看个人信息
     * @param request 请求
     * @return 返回个人信息
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/me")
    public AjaxResult<UserInfo> viewMe(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        log.info("View user info: "+user);
        return AjaxResult.success(userService.viewMe(user));
    }
}
