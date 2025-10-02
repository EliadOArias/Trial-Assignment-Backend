package com.github.eliadoarias.tgb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 改帖请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUpdateRequest {
    /**
     * 表白的标题
     * @titleName 标题
     * @example I love money
     */
    @Size(min=1,max=50)
    @NotBlank
    private String title;

    /**
     * 表白的内容
     * @titleName 内容
     * @example Money is beautiful and kind.
     */
    @Size(min=1,max=200)
    @NotBlank
    private String content;

    /**
     * 一组图片的url
     * @titleName 图片列表
     */
    @Size(min=0,max=9)
    private List<String> photos;

    /**
     * 帖子是否公开
     * @titleName 公开状态
     */
    private Boolean open;
}
