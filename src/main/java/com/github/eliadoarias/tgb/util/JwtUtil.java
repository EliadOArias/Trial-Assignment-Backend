package com.github.eliadoarias.tgb.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.eliadoarias.tgb.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

    @Resource
    private JwtConfig jwtConfig;

    public String generateAccessToken(String userId) {
        return Jwts.builder()
                //header
                .header().type("JWT").and()
                //claims
                .claims()
                .issuer("Server")
                .issuedAt(TimeUtil.getDateNow())
                .expiration(TimeUtil.getDateAfter(jwtConfig.getExpirationTime()))
                .subject(userId)
                //claims/audience
                .audience().add("User").and()
                .and()
                //sigh
                .signWith(jwtConfig.getKey())
                .compact();
    }

    public String generateRefreshToken(String userId,
                                       long expiration) {
        return Jwts.builder()
                //header
                .header().type("JWT").and()
                //claims
                .claims()
                .issuer("Server")
                .issuedAt(TimeUtil.getDateNow())
                .expiration(TimeUtil.getDateAfter(expiration))
                .subject(userId)
                //claims/audience
                .audience().add("User").and()
                .and()
                //sigh
                .signWith(jwtConfig.getKey())
                .compact();
    }

    public JwtReader readToken(String token) {
        log.info(token);
        if (token == null) { return null; }
        if (token.startsWith("Bearer ")) {
            String bearerToken = token.substring(7);
            if (StringUtils.isEmpty(bearerToken)) { return null; }
            return new JwtReader(token.substring(7),jwtConfig);
        }
        else { return new JwtReader(token,jwtConfig); }
    }
}
