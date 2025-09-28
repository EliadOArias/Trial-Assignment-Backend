package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录token
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenInfo {
    /**
     * 返回的登录token，存在时间较短，到期后需要前端用刷新token发送获取新token的请求
     * @titleName 登录token
     * @example xxx.yyy.zzz
     */
    @JsonProperty("access-token")
    private String accessToken;
    /**
     * 返回的刷新token，存在时间较长，用于申请新的登录token
     * @titleName 刷新token
     * @example aaa.bbb.ccc
     */
    @JsonProperty("refresh-token")
    private String refreshToken;
}
