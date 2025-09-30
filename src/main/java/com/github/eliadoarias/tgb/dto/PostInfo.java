package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.eliadoarias.tgb.entity.Confession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostInfo {
    /**
     * 表白id
     * @titleName 表白ID
     * @example 1
     */
    private Integer id;
    /**
     * 表白的标题
     * @titleName 表白标题
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
     * @titleName 观看数
     */
    private Integer views;
    /**
     * @titleName 点赞数
     */
    private Integer likes;
    /**
     * @titleName 投稿者用户名
     * @example aaa-bbb-ccc-ddd
     */
    @JsonProperty("poster_name")
    private String posterName;
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
     * 访问用户对该表白是否点赞
     * @titleName 点赞状态
     */
    private boolean liked;

    /**
     * 没有设定部分变量！
     * @param confession
     * @return
     */
    public static PostInfo of(Confession confession){
        return builder()
                .id(confession.getId())
                .title(confession.getTitle())
                .content(confession.getContent())
                .photos(List.of(confession.getPhotos().split(",")))
                .posterName("Anonymous-User")
                .views(confession.getViews())
                .likes(confession.getLikes())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }
}
