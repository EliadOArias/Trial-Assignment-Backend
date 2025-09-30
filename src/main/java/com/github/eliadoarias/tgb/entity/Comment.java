package com.github.eliadoarias.tgb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment {
    private Integer id;

    private Integer postId;

    private Integer userId;

    private Integer parentId;

    private Integer rootId;

    private String content;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}