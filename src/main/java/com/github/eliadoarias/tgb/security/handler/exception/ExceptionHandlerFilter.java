package com.github.eliadoarias.tgb.security.handler.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.result.AjaxResult;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("WHY NOT DO THIS???");
        try {
            filterChain.doFilter(request, response);
        }
        catch (AuthorizationDeniedException e) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(AjaxResult.error(ExceptionEnum.UNAUTHORIZED)));
            out.flush();
            out.close();
        }
        catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}
