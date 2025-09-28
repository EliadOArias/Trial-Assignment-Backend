package com.github.eliadoarias.tgb.security.handler.authentication.login.username;

import com.github.eliadoarias.tgb.security.LoginUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UsernameAuthentication extends AbstractAuthenticationToken {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private LoginUser loginUser;

    public UsernameAuthentication() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : password;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? loginUser : username;
    }
}
