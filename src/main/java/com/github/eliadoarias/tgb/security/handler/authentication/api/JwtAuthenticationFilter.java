package com.github.eliadoarias.tgb.security.handler.authentication.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.security.LoginUser;
import com.github.eliadoarias.tgb.security.SecurityConfig;
import com.github.eliadoarias.tgb.util.ExceptionUtil;
import com.github.eliadoarias.tgb.util.JwtReader;
import com.github.eliadoarias.tgb.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtUtil jwtUtil;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if(Arrays.asList(SecurityConfig.AUTH_WHITELIST).contains(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }
        log.info("拦截: "+request.getRequestURI());
        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtReader reader = jwtUtil.readToken(token);
        String userId = reader.getSubject();
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("user_id", userId);
        User user = userMapper.selectOne(wrapper);
        request.setAttribute("user", user);
        LoginUser loginUser = new LoginUser(user);

        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();

        JwtAuthentication authentication = new JwtAuthentication(authorities);
        authentication.setToken(token);
        authentication.setLoginUser(loginUser);
        authentication.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("通过："+request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}