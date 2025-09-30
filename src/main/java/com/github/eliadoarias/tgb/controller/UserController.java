package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.config.JwtConfig;
import com.github.eliadoarias.tgb.dto.*;
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
        return AjaxResult.success(userService.register(request));
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
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.view(userId));
    }

    /**
     * 查看用户信息
     * @param username 用户名
     * @return data为对方的用户信息
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/{username}")
    public AjaxResult<UserInfo> view(@PathVariable("username") String username) {
        return AjaxResult.success(userService.viewByName(username));
    }

    /**
     * 更新用户信息
     * 包括用户的头像等
     * @return data为对方的用户信息
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @PutMapping("/me")
    public AjaxResult<UserInfo> update(
            @RequestBody UserUpdateRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.update(dto, userId));
    }

    /**
     * 拉黑用户
     * @param username 用户名
     * @param request 请求
     * @return 无
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @PutMapping("/blacklist/{username}")
    public AjaxResult<Object> blacklistAdd(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.blacklistAdd(username,userId));
    }

    /**
     * 取消拉黑用户
     * @param username 用户名
     * @param request 请求
     * @return 无
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @DeleteMapping("/blacklist/{username}")
    public AjaxResult<Object> blacklistDelete(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.blacklistDelete(username,userId));
    }
}
