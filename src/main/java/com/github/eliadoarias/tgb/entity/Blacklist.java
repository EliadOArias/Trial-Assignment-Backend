package com.github.eliadoarias.tgb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName blacklist
 */
@TableName(value ="blacklist")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Blacklist {
    @TableId(type =  IdType.AUTO)
    private Integer id;

    private Integer sourceUserId;

    private Integer targetUserId;
}