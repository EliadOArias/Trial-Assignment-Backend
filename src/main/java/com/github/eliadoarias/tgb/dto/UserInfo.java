package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    /**
     * 用户名，一般要求为英文、无特殊字符
     * @titleName 用户名
     * @example SugarSMG
     */
    private String username;
    /**
     * 用户的昵称
     * @titleName 昵称
     * @example 白糖撒一地
     */
    private String name;
    /**
     * 用户的外部id
     * @titleName 用户id
     * @example aaa-bbb-ccc-ddd
     */
    @JsonProperty("user_id")
    private String userId;
    /**
     * 用户的身份类型，一个位掩码，控制身份鉴权
     * @titleName 昵称
     * @example 1
     */
    private Integer usertype;
    /**
     * 用户的头像绝对url
     * @titleName 头像
     */
    private String avatar;
}
