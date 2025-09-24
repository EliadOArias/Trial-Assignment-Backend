package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.config.JwtConfig;
import com.github.eliadoarias.tgb.dto.LoginResponse;
import com.github.eliadoarias.tgb.dto.RegisterRequest;
import com.github.eliadoarias.tgb.dto.UserInfo;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public AjaxResult<LoginResponse> register(@Valid @RequestBody RegisterRequest request,
                                              HttpSession session,
                                              HttpServletResponse response) {
        log.info("Try register: "+request.getUsername());
        return AjaxResult.success(userService.register(request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getUsertype()));
    }

    @PostMapping("/login")
    public AjaxResult<Object> login() {
        log.info("should never do here ");
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public AjaxResult<UserInfo> viewMe(@RequestAttribute User user) {
        log.info("View user info: "+user);
        return AjaxResult.success(userService.viewMe(user));
    }
}
