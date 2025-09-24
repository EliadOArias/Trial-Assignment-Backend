package com.github.eliadoarias.tgb.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.util.JwtReader;
import com.github.eliadoarias.tgb.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
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

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("username", "password", "ROLE_USER")
        );
        log.info("通过："+request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}