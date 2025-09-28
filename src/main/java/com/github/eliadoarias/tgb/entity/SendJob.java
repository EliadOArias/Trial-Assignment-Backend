package com.github.eliadoarias.tgb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName send_job
 */
@TableName(value ="send_job")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SendJob {
    @TableId(type =  IdType.AUTO)
    private Integer id;

    private Integer sendId;

    private LocalDateTime sendTime;
}