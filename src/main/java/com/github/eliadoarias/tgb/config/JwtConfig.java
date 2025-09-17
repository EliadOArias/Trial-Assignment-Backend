package com.github.eliadoarias.tgb.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@ConfigurationProperties(prefix = "jwt")
@Data
@Component
public class JwtConfig {
    private String baseKey;
    private SecretKey key;
    private long expirationTime;
    private long refreshTime;
    private long refreshLimit;

    public void setBaseKey(String baseKey) {
        this.setKey(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)));
        this.baseKey = baseKey;
    }
    public SecretKey getKey() {
        if(key == null){
            key = Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }
}
