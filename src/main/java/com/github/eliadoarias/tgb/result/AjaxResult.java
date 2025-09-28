package com.github.eliadoarias.tgb.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 基本返回结果
 * @param <T> DATA中包含的元素
 */
@Data
@AllArgsConstructor
public class AjaxResult<T> {
    public final static String SUCCESS = "success";

    /**
     * 返回代码
     * @titleName 返回代码
     * @example 200
     */
    private int code;

    /**
     * 返回消息
     * @titleName 返回消息
     * @example success
     */
    @JsonProperty("msg")
    private String message;

    /**
     * 返回数据
     * @titleName 返回数据
     */
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
