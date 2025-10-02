package com.github.eliadoarias.tgb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    /**
     * 用户名，一般要求为英文、无特殊字符
     * @titleName 用户名
     * @example SugarSMG
     */
    @NotBlank
    @NotNull
    private String username;
    /**
     * 用户的密码，一般要求为ascii码
     * @titleName 密码
     * @example Sugar123456
     */
    @NotBlank
    @NotNull
    private String password;
}
