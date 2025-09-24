package com.github.eliadoarias.tgb.security.handler.authentication.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.LoginResponse;
import com.github.eliadoarias.tgb.dto.UserInfo;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.security.LoginUser;
import com.github.eliadoarias.tgb.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class LoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements AuthenticationSuccessHandler {

    @Resource
    JwtUtil jwtUtil;

    @Resource
    ApplicationEventPublisher applicationEventPublisher;

    public LoginSuccessHandler() {
        this.setRedirectStrategy(
                new RedirectStrategy() {
                    @Override
                    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

                    }
                }
        );
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if(principal == null)
            throw new ApiException(ExceptionEnum.LOGIN_ERROR);
        LoginUser loginUser = (LoginUser) principal;
        LoginResponse loginResponse = new LoginResponse(
                jwtUtil.generateAccessToken(loginUser.getUserId()),
                jwtUtil.generateRefreshToken(loginUser.getUserId(), 60 * 60 * 1000)
        );
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new ObjectMapper().writeValueAsString(AjaxResult.success(loginResponse)));
        out.flush();
        out.close();
        log.info("Passed user in filter: {}", loginUser.getUsername());
    }
}
