package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.eliadoarias.tgb.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 回复信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentInfo {
    /**
     * 回复的id
     * @titleName id
     * @example 1
     */
    private Integer id;

    /**
     * 回复的帖子的id
     * @titleName 帖子id
     * @example 1
     */
    @JsonProperty("post_id")
    private Integer postId;

    /**
     * 回复者的名字
     * @titleName 用户名
     */
    private String username;

    /**
     * 评论的内容
     * @titleName 内容
     * @example Hello World!
     */
    private String content;

    /**
     * 被回复的评论的id
     * @titleName 父评论id
     * @example 1
     */
    @JsonProperty("parent_id")
    private Integer parentId;

    /**
     * 回复链的根评论的id
     * @titleName 根评论id
     * @example 1
     */
    @JsonProperty("root_id")
    private Integer rootId;

    /**
     * @titleName 创建时间
     */
    @JsonProperty("create_at")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    /**
     * @titleName 更新时间
     */
    @JsonProperty("update_at")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    /**
     * 生产一个Comment info（不完整，缺少username）
     * @param comment
     * @return
     */
    public static CommentInfo of(Comment comment) {
        return builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .rootId(comment.getRootId())
                .createAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .build();
    }
}
