package com.github.eliadoarias.tgb.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    SERVER_ERROR(500, "服务器错误"),
    //Not found
    NOT_FOUND(404, "Not Found"),
    USER_NOT_FOUND(404, "目标用户不存在"),
    POST_NOT_FOUND(404, "目标表白不存在"),
    COMMENT_NOT_FOUND(404, "目标评论不存在"),
    IMAGE_NOT_FOUND(404, "目标图片不存在"),
    //Token
    TOKEN_EXP(2201, "token已过期"),
    TOKEN_MISTAKE(2202, "token错误"),
    //Register

    REGISTER_DUPLICATED(2310, "用户名已存在"),
    //Login
    LOGIN_ERROR(2401, "账号或密码错误"),
    //Authentication
    UNAUTHENTICATED(2001, "认证失败"),
    UNAUTHORIZED(2002, "无权访问"),
    //Image
    IMAGE_URL_ERROR(2501,"图片url格式错误"),
    //Blacklist
    BLACKLIST_DUPLICATED(2601,"拉黑目标为自己"),
    BLACKLIST_ADDED(2602,"用户已经被拉黑"),
    BLACKLIST_NOT_EXISTS(2603,"用户没有被拉黑"),
    //Post

    POST_UPDATE_NOOP(2703,"无权限修改这个帖子，因为这个帖子不是你发布且你不是管理员"),
    //Comment


    INVALID_PARAMETERS(2003, "参数错误");




    private final Integer code;
    private final String message;
}
