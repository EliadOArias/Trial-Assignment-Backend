package com.github.eliadoarias.tgb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private Integer usertype;
}
