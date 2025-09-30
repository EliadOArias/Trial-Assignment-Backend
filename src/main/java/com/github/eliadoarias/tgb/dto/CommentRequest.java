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
public class CommentRequest {
    /**
     * 若为某个评论的回复，则为那个评论的id，否则为0
     * @titleName 回复评论id
     */
    @Nullable
    private Integer parentId;
    /**
     * 评论的内容
     * @titleName 内容
     * @example Hello World!
     */
    private String content;
}
