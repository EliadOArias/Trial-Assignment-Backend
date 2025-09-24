package com.github.eliadoarias.tgb.security;

import com.github.eliadoarias.tgb.security.handler.authentication.login.LoginFailHandler;
import com.github.eliadoarias.tgb.security.handler.authentication.login.LoginSuccessHandler;
import com.github.eliadoarias.tgb.security.handler.authentication.login.username.UsernameAuthenticationFilter;
import com.github.eliadoarias.tgb.security.handler.authentication.login.username.UsernameAuthenticationProvider;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final static String[] AUTH_WHITELIST = {
            "/api/users/register",
            "/api/users/login"
    };

    @Resource
    private JwtAuthFilter jwtAuthFilter;

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler, LoginFailHandler loginFailHandler) throws Exception {
        LoginSuccessHandler successHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler failHandler = applicationContext.getBean(LoginFailHandler.class);
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        UsernameAuthenticationFilter usernameLoginFilter = new UsernameAuthenticationFilter(
                PathPatternRequestMatcher.withDefaults().basePath("/api").matcher("/users/login"),
                new ProviderManager(
                        List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))
                ),
                loginSuccessHandler,
                loginFailHandler
        );
        http.addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}