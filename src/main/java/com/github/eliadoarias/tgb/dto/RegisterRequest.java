package com.github.eliadoarias.tgb.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    /**
     * 用户名，一般要求为英文、无特殊字符
     * @titleName 用户名
     * @example SugarSMG
     */
    @Size(min=6, max=20)
    @NotNull
    @Pattern(regexp = "^[\\x20-\\x7E]+$")
    private String username;
    /**
     * 用户的密码，一般要求为ascii码
     * @titleName 密码
     * @example Sugar123456
     */
    @Size(min=6, max=20)
    @NotNull
    @Pattern(regexp = "^[\\x20-\\x7E]+$")
    private String password;
    /**
     * 用户的昵称
     * @titleName 昵称
     * @example 白糖撒一地
     */
    @NotBlank
    @Size(min=1,max=20)
    @NotNull
    private String name;
    /**
     * 用户的身份类型，一个位掩码，控制身份鉴权
     * @titleName 昵称
     * @example 1
     */
    @NotNull
    private Integer usertype;
    /**
     * 用户的头像url
     * @titleName 头像
     */
    @NotNull
    private String avatar;
}
