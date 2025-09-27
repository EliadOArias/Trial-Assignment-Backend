package com.github.eliadoarias.tgb.security;

import com.github.eliadoarias.tgb.handler.CustomerAccessDeniedHandler;
import com.github.eliadoarias.tgb.security.handler.authentication.api.JwtAuthenticationFilter;
import com.github.eliadoarias.tgb.security.handler.authentication.login.LoginFailHandler;
import com.github.eliadoarias.tgb.security.handler.authentication.login.LoginSuccessHandler;
import com.github.eliadoarias.tgb.security.handler.authentication.login.username.UsernameAuthenticationFilter;
import com.github.eliadoarias.tgb.security.handler.authentication.login.username.UsernameAuthenticationProvider;
import com.github.eliadoarias.tgb.security.handler.exception.ExceptionHandlerFilter;
import jakarta.annotation.Resource;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public final static String[] AUTH_WHITELIST = {
            "/api/users/register",
            "/api/users/login"
    };

    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Resource
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;

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
    @Order(1)
    public SecurityFilterChain loginFilterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler, LoginFailHandler loginFailHandler) throws Exception {
        LoginSuccessHandler successHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler failHandler = applicationContext.getBean(LoginFailHandler.class);
        doCommon(http);
        UsernameAuthenticationFilter usernameLoginFilter = new UsernameAuthenticationFilter(
                PathPatternRequestMatcher.withDefaults().basePath("/api").matcher("/users/login"),
                new ProviderManager(
                        List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))
                ),
                loginSuccessHandler,
                loginFailHandler,
                resolver
        );
        http.securityMatcher("/api/users/login")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll())
                .addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain registerFilterChain(HttpSecurity http) throws Exception {
        doCommon(http);
        http.securityMatcher("/api/users/register")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll());;
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain businessFilterChain(HttpSecurity http) throws Exception {
        doCommon(http);
        http.securityMatcher("/api/**")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(10)
    public SecurityFilterChain ResourceFilterChain(HttpSecurity http) throws Exception {
        doCommon(http);
        http.securityMatcher("/resources/**")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll());
        return http.build();
    }

    private void doCommon(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(customerAccessDeniedHandler))
                .addFilterAfter(exceptionHandlerFilter, SecurityContextHolderFilter.class);
    }
}