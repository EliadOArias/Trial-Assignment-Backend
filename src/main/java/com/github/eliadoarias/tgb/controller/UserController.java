package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.config.JwtConfig;
import com.github.eliadoarias.tgb.dto.*;
import com.github.eliadoarias.tgb.exception.ApiException;
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
     * 由于白糖的指示，所有有关参数的异常被集成到INVALID_PARAMETERS中。具体规则在接口文档的数据结构中，须前端自行解析。
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * USERTYPE_ERROR(2309, "没有足够权限创建该类型的用户或用户类型不存在"),
     * REGISTER_DUPLICATED(2310, "用户名已存在"),
     * IMAGE_URL_ERROR(2501,"图片url格式错误")
     * @param dto 包含用户基本信息
     * @return 返回登录token和刷新token
     */
    @PostMapping("/register")
    public AjaxResult<TokenInfo> register(@Valid @RequestBody RegisterRequest dto) {
        log.info("Try register: "+dto.getUsername());
        return AjaxResult.success(userService.register(dto));
    }

    /**
     * 登录
     * 提供账号名和密码进行登录，返回token。
     * 前端需要存储token。
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * LOGIN_ERROR(2401, "账号或密码错误")
     * @param dto 数据包
     * @return 返回登录token和刷新token
     */
    @PostMapping("/login")
    public AjaxResult<TokenInfo> login(@Valid @RequestBody LoginRequest dto) {
        log.info("should never do here ");
        return null;
    }

    /**
     * 查看个人信息
     * 查看个人的用户信息
     * 可能的异常：
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
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
     * 可能的异常：
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问"),
     * USER_NOT_FOUND(404, "目标用户不存在")
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
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问"),
     * IMAGE_URL_ERROR(2501,"图片url格式错误")
     * @return data为对方的用户信息
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @PutMapping("/me")
    public AjaxResult<UserInfo> update(
            @Valid @RequestBody UserUpdateRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.update(dto, userId));
    }

    /**
     * 拉黑选择的用户
     * 可能的异常：
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * USER_NOT_FOUND(404, "目标用户不存在"),
     * UNAUTHORIZED(2002, "无权访问"),
     * BLACKLIST_DUPLICATED(2601,"拉黑目标为自己"),
     * BLACKLIST_ADDED(2602,"用户已经被拉黑")
     * @param username 用户名
     * @param request 请求
     * @return 无
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @PostMapping("/blacklist/{username}")
    public AjaxResult<Object> blacklistAdd(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.blacklistAdd(username,userId));
    }

    /**
     * 取消拉黑用户
     * 可能的异常：
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * USER_NOT_FOUND(404, "目标用户不存在"),
     * UNAUTHORIZED(2002, "无权访问"),
     * BLACKLIST_NOT_EXISTS(2603,"用户没有被拉黑"),
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

    /**
     * 获取黑名单
     * 可能的异常：
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param request 请求
     * @return 无
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/blacklist")
    public AjaxResult<BlacklistInfo> blacklistGet(
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(userService.blacklistGet(userId));
    }

    /**
     * 刷新token
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误")
     * @return 无
     */
    @GetMapping("/refresh-token")
    public AjaxResult<TokenInfo> refreshToken(
            @Valid @RequestBody RefreshRequest dto
    ) {
        return AjaxResult.success(userService.refresh(dto.getRefreshToken()));
    }
}
