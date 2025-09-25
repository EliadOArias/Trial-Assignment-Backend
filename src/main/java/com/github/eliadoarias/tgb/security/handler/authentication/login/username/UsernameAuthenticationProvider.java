package com.github.eliadoarias.tgb.security.handler.authentication.login.username;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.UserInfo;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.security.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsernameAuthenticationProvider implements AuthenticationProvider {
    private final ObjectMapper mapper = new ObjectMapper();

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (username == null || password == null) {
            throw new ApiException(ExceptionEnum.INVALID_PARAMETERS);
        }
        else if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiException(ExceptionEnum.LOGIN_ERROR);
        }
        log.info("Login Success:{}",username);

        UsernameAuthentication token = new UsernameAuthentication();
        token.setLoginUser((LoginUser) user);
        token.setAuthenticated(true);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernameAuthentication.class);
    }
}
