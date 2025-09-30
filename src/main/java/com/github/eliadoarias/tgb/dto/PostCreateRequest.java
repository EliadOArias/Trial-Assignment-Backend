package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发帖请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCreateRequest {
    /**
     * 表白的标题
     * @titleName 标题
     * @example I love money
     */
    private String title;

    /**
     * 表白的内容
     * @titleName 内容
     * @example Money is beautiful and kind.
     */
    private String content;

    /**
     * 一组图片的url
     * @titleName 图片列表
     */
    private List<String> photos;

    /**
     * 帖子将要发布的时间，小于当前时间则立即发布
     * @titleName 预定发布时间
     */
    @JsonProperty("send_time")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    /**
     * 帖子是否公开
     * @titleName 公开状态
     */
    private boolean open;

    /**
     * 帖子是否匿名
     * @titleName 匿名状态
     */
    private boolean anonymous;
}
