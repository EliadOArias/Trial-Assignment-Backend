package com.github.eliadoarias.tgb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送评论请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepliesRequest {
    /**
     * 评论的内容
     * @titleName 内容
     * @example Hello World!
     */
    @Size(min=1,max=200)
    @NotNull
    @NotBlank
    private String content;
}
