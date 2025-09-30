package com.github.eliadoarias.tgb.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    SERVER_ERROR(500, "服务器错误"),
    NOT_FOUND(404, "Not Found"),
    USER_NOT_FOUND(404, "目标用户不存在"),
    POST_NOT_FOUND(404, "目标表白不存在"),
    WRONG_TOKEN(2200, "Wrong Token"),
    UNAUTHENTICATED(2001, "认证失败"),
    UNAUTHORIZED(2002, "无权访问"),
    LOGIN_ERROR(2401, "账号或密码错误"),
    REGISTER_DUPLICATED(2402, "账号已存在"),
    USER_EXP(2100, "token已过期"),
    INVALID_PARAMETERS(2003, "参数错误"),

    BLACKLIST_DUPLICATED(2300,"不能拉黑自己");


    private final Integer code;
    private final String message;
}
