package com.github.eliadoarias.tgb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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
    private String content;
}
