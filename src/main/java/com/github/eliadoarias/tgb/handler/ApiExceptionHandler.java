package com.github.eliadoarias.tgb.handler;

import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.util.ExceptionUtil;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(50)
public class ApiExceptionHandler {
    @ExceptionHandler({
            ApiException.class,
    })
    @ResponseBody
    public AjaxResult<Object> handleApiException(ApiException e) {
        ExceptionUtil.printError (e);
        return AjaxResult.error(e.getCode(), e.getMessage());
    }
}
