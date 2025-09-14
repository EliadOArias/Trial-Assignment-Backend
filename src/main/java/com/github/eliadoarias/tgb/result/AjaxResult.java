package com.github.eliadoarias.tgb.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class AjaxResult<T> {
    public final static String SUCCESS = "success";
    private int code;
    @JsonProperty("msg")
    private String message;
    private T data;

    public static <N> AjaxResult<N> success(){
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS, null);
    }


    public static <N> AjaxResult<N> success(N data){
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS, data);
    }

    public static <N> AjaxResult<N> error(Integer code, String message){
        return new AjaxResult<>(code, message, null);
    }

    public static <N> AjaxResult<N> error(ExceptionEnum e){
        return new AjaxResult<>(e.getCode(), e.getMessage(), null);
    }
}
