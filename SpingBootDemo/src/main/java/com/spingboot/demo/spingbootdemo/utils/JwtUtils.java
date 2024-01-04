package com.spingboot.demo.spingbootdemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtUtils {
    private static final String secret = "U3BpbmcgYm9vdCBUZXN0"; // 密钥

    /**
     * token有效时间
     */
    private static final Integer expirationDay = 30;
    // 生成令牌
    public static String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expirationDate()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 获取token指定有效时间
     * @return
     */
    private static Instant expirationDate(){
        return Instant.now().plus(expirationDay, ChronoUnit.DAYS);
    }

    /**
     * 验证令牌
     *
     * @param token
     * @param userId
     * @param idList
     * @return
     */
    public static boolean validateToken(String token, String userId, String[] idList) {
        Claims claims = getClaimsFromToken(token);
        return checkUid(userId, idList) && userId.equals(claims.getSubject()) && !isTokenExpired(token);
    }

    public static boolean validateToken2(String token) {
        return !isTokenExpired(token);
    }

    // 从令牌中获取 Claims
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否过期
    public static boolean isTokenExpired(String token) {
        Date expirationDate;
        try {
            expirationDate = getClaimsFromToken(token).getExpiration();
        } catch (Exception exception) {
            return true;
        }
        return expirationDate.before(new Date());
    }

    public static String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("token");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        } // The part after "Bearer "
        return null;
    }


    /**
     * 检查缓存数据中是否存在该 用户ID
     *
     * @param userId
     * @param list
     * @return
     */
    public static boolean checkUid(String userId, String[] list) {
        boolean checkUid = false;
        if (list != null) {
            for (String uid : list) {
                if (uid.equals(userId)) {
                    checkUid = true;
                    break;
                }
            }
        }
        return checkUid;
    }
}

