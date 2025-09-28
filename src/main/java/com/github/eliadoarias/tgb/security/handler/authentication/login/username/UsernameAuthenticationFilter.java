package com.github.eliadoarias.tgb.security.handler.authentication.login.username;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eliadoarias.tgb.dto.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HandlerExceptionResolver resolver;

    public UsernameAuthenticationFilter(
            PathPatternRequestMatcher matcher,
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            HandlerExceptionResolver resolver) {
        super(matcher);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
        this.resolver = resolver;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try{
            String requestJsonData = request.getReader().lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            LoginRequest loginRequest = objectMapper.readValue(requestJsonData, LoginRequest.class);
            log.debug("loginRequest: {}", loginRequest.getUsername());
            com.github.eliadoarias.tgb.security.handler.authentication.login.username.UsernameAuthentication authentication = new com.github.eliadoarias.tgb.security.handler.authentication.login.username.UsernameAuthentication();
            authentication.setUsername(loginRequest.getUsername());
            authentication.setPassword(loginRequest.getPassword());
            authentication.setAuthenticated(false);
            return getAuthenticationManager().authenticate(authentication);
        }catch (Exception e){
            resolver.resolveException(request, response, null, e);
            return null;
        }
    }
}
