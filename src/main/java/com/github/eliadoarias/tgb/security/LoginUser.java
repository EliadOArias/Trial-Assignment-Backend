package com.github.eliadoarias.tgb.security;

import com.github.eliadoarias.tgb.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    public static final int ROLE_USER = 0b0001;

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if ((ROLE_USER & user.getUsertype()) != 0){
            authorities.add(new SimpleGrantedAuthority("permission:user.read"));
            authorities.add(new SimpleGrantedAuthority("permission:user.upload"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    public String getUserId() { return user.getUserId(); }
}
