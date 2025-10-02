package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Size(min=1,max=50)
    @NotNull
    @NotBlank
    private String title;

    /**
     * 表白的内容
     * @titleName 内容
     * @example Money is beautiful and kind.
     */
    @Size(min=1,max=200)
    @NotNull
    @NotBlank
    private String content;

    /**
     * 一组图片的url
     * @titleName 图片列表
     */
    @Size(min=0,max=9)
    @NotNull
    private List<String> photos;

    /**
     * 帖子将要发布的时间，小于当前时间则立即发布
     * @titleName 预定发布时间
     */
    @JsonProperty("send_time")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime sendTime;

    /**
     * 帖子是否公开
     * @titleName 公开状态
     */
    @NotNull
    private boolean open;

    /**
     * 帖子是否匿名
     * @titleName 匿名状态
     */
    @NotNull
    private boolean anonymous;
}
