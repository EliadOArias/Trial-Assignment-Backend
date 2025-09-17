package com.github.eliadoarias.tgb.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    NOT_FOUND(404, "Not Found"),
    WRONG_TOKEN(2001, "Wrong Token"),
    LOGIN_ERROR(2401, "账号或密码错误"),
    REGISTER_DUPLICATED(2402, "账号已存在");;


    private final Integer code;
    private final String message;
}
