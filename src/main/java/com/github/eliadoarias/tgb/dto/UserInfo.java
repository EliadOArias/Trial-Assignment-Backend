package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    private String username;
    private String name;
    @JsonProperty("user_id")
    private String userId;
    private Integer usertype;
}
