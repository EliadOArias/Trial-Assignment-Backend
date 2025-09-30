package com.github.eliadoarias.tgb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 黑名单信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BlacklistInfo {
    private List<String> users;
}
