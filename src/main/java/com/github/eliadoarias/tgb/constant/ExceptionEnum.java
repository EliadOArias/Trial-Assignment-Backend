package com.github.eliadoarias.tgb.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    NOT_FOUND(404, "Not Found"),;

    private final Integer code;
    private final String message;
}
