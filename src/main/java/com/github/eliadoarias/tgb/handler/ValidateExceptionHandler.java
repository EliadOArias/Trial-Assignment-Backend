package com.github.eliadoarias.tgb.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.util.ExceptionUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Order(10)
public class ValidateExceptionHandler {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            JsonMappingException.class,
            HttpMessageNotReadableException.class,
            ServletRequestBindingException.class
    })
    @ResponseBody
    public AjaxResult<Object> handleValidateException(Exception e) {
        ExceptionUtil.printError(e);
        return AjaxResult.error(ExceptionEnum.INVALID_PARAMETERS);
    }

    @ExceptionHandler({
            NoResourceFoundException.class,
            HttpRequestMethodNotSupportedException.class
    })
    @ResponseBody
    public AjaxResult<Object> handleNotFoundException(Exception e) {
        ExceptionUtil.printError(e);
        return AjaxResult.error(ExceptionEnum.NOT_FOUND);
    }

    @ExceptionHandler({
            ExpiredJwtException.class
    })
    @ResponseBody
    public AjaxResult<Object> handleExpiredJwtException(Exception e) {
        ExceptionUtil.printError(e);
        return AjaxResult.error(ExceptionEnum.USER_EXP);
    }
}
