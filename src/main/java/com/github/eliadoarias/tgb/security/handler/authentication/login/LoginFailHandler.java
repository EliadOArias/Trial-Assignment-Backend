package com.github.eliadoarias.tgb.security.handler.authentication.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.result.AjaxResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg = exception.getMessage();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        AjaxResult<Object> responseData = AjaxResult.error(ExceptionEnum.LOGIN_ERROR);
        out.write(new ObjectMapper().writeValueAsString(responseData));
        out.flush();
        out.close();
    }
}
