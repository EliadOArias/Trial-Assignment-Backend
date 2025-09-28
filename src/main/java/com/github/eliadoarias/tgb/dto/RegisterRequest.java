package com.github.eliadoarias.tgb.dto;

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
    private String username;
    /**
     * 用户的密码，一般要求为ascii码
     * @titleName 密码
     * @example Sugar123456
     */
    private String password;
    /**
     * 用户的昵称
     * @titleName 昵称
     * @example 白糖撒一地
     */
    private String name;
    /**
     * 用户的身份类型，一个位掩码，控制身份鉴权
     * @titleName 昵称
     * @example 1
     */
    private Integer usertype;
}
