package com.github.eliadoarias.tgb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @TableName confession
 */
@TableName(value ="confession")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Confession {
    @TableId(type =  IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    private String photos;

    private Integer views;

    private Integer likes;

    private String posterId;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private boolean unsent;
}