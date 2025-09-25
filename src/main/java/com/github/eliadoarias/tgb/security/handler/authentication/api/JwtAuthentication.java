package com.github.eliadoarias.tgb.security.handler.authentication.api;

import com.github.eliadoarias.tgb.security.LoginUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {

    @Getter @Setter
    private String token;
    @Getter @Setter
    private LoginUser loginUser;

    public JwtAuthentication() {
        super(null);
    }

    public JwtAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : token;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? loginUser : token;
    }
}
