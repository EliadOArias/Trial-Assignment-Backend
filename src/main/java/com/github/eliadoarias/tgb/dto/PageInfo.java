package com.github.eliadoarias.tgb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 页信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PageInfo {
    /**
     * 当前页帖子的集合
     * @titleName 本页帖子
     */
    private List<PostInfo> posts;
    /**
     * 帖子总数量
     * @titleName 总贴数
     */
    private Long total;
    /**
     * 页数
     * @titleName 总页数
     */
    private Long pages;
    /**
     * 当前页的页码
     * @titleName 页码
     */
    private Long current;
}
