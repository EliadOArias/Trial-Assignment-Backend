package com.github.eliadoarias.tgb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token刷新请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshRequest {
    /**
     * 用于刷新的token
     * @titleName 刷新token
     */
    private String refreshToken;
}
