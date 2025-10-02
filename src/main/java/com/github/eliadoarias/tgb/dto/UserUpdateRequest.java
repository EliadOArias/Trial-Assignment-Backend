package com.github.eliadoarias.tgb.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新用户信息请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    /**
     * 用户名，一般要求为英文、无特殊字符
     * @titleName 用户名
     * @example SugarSMG
     */
    @Size(min=6,max=20)
    private String username;
    /**
     * 用户的昵称
     * @titleName 昵称
     * @example 白糖撒一地
     */
    @Size(min=1,max=20)
    private String name;
    /**
     * 用户的头像绝对url
     * @titleName 头像
     */
    private String avatar;
}
