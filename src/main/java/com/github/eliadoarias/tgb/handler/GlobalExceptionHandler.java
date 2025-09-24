package com.github.eliadoarias.tgb.handler;

import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.util.ExceptionUtil;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(1000)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResult<Object> handleGlobalException(Exception e) {
        ExceptionUtil.printError(e);
        return AjaxResult.error(ExceptionEnum.SERVER_ERROR);
    }
}
