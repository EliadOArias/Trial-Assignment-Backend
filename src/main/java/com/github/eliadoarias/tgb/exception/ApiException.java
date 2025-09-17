package com.github.eliadoarias.tgb.exception;

import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final Integer code;
    private final String message;

    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ApiException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public ApiException(ExceptionEnum exceptionEnum){
        this(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    public ApiException(ExceptionEnum exceptionEnum, Throwable cause) {
        this(exceptionEnum.getCode(), exceptionEnum.getMessage(), cause);
    }
}
