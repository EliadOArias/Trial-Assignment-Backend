package com.github.eliadoarias.tgb.util;

import com.github.eliadoarias.tgb.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;


public class JwtReader {
    private String token;
    private final Claims claims;

    public JwtReader(String token, JwtConfig jwtConfig) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(jwtConfig.getKey())
                .build();
        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        claims = jws.getPayload();
    }

    public String getSubject() {
        return claims.getSubject();
    }

    public String getId() {
        return claims.getId();
    }
}
