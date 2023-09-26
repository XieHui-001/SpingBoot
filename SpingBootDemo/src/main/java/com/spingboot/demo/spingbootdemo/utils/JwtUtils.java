package com.spingboot.demo.spingbootdemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static JwtUtils instance;
    public static JwtUtils getInstance(){
        if (instance == null){
            instance = new JwtUtils();
        }
        return instance;
    }

    private final String secret = "U3BpbmcgYm9vdCBUZXN0"; // 替换为实际的密钥
    private final long expiration = 3600 * 1000; // 令牌过期时间，以毫秒为单位

    // 生成令牌
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 验证令牌
    public boolean validateToken(String token, String userId) {
        Claims claims = getClaimsFromToken(token);
        return userId.equals(claims.getSubject()) && !isTokenExpired(token);
    }

    // 从令牌中获取 Claims
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否过期
    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaimsFromToken(token).getExpiration();
        return expirationDate.before(new Date());
    }
}

