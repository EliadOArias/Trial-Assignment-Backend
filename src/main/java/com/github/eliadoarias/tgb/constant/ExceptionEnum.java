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
    PASSWORD_TOO_SHORT(2301, "密码短于6个字符"),
    PASSWORD_TOO_LONG(2302, "密码长于20个字符"),
    PASSWORD_INVALID_CHA(2303, "密码包含非法字符（只能包含英文字符和部分符号）"),
    USERNAME_TOO_SHORT(2304, "用户名短于6个字符"),
    USERNAME_TOO_LONG(2305, "用户名长于20个字符"),
    USERNAME_INVALID_CHA(2306, "用户名包含非法字符（只能包含英文字符和和部分符号）"),
    NAME_TOO_SHORT(2307, "昵称为空"),
    NAME_TOO_LONG(2308, "昵称长于20个字符"),
    USERTYPE_ERROR(2309, "没有足够权限创建该类型的用户或用户类型不存在"),
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
    POST_CONTENT_TOO_LONG(2701,"表白内容超过200字符"),
    POST_TITLE_TOO_LONG(2702,"表白标题超过50字符"),
    POST_UPDATE_NOOP(2703,"无权限修改这个帖子，因为这个帖子不是你发布且你不是管理员"),
    //Comment
    COMMENT_CONTENT_TOO_LONG(2801,"评论内容超过200字符"),

    INVALID_PARAMETERS(2003, "参数错误");




    private final Integer code;
    private final String message;
}
