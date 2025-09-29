package com.github.eliadoarias.tgb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取表白
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostGetRequest {
    /**
     * 第几页
     * @titleName 页码
     * @example 0
     */
    private Integer page;
    /**
     * 此页帖子数量
     * @titleName 数量
     * @example 10
     */
    private Integer size;
}
