package com.spingboot.demo.spingbootdemo.utils;

import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.redis.RedisService;
import com.spingboot.demo.spingbootdemo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtils {
    private JwtExpirationCallback callback;

    public void setCallback(JwtExpirationCallback callback){
        this.callback = callback;
    }

    private static final String secret = "U3BpbmcgYm9vdCBUZXN0"; // 密钥

    private static final long expiration = 3600 * 1000; // 令牌过期时间，以毫秒为单位

    // 生成令牌
    public static String generateToken(String userId) {
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
    public static boolean validateToken(String token, String userId,String[] idList) {
        Claims claims = getClaimsFromToken(token);
        return checkUid(userId,idList) && userId.equals(claims.getSubject()) && !isTokenExpired(token,null);
    }

    // 从令牌中获取 Claims
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否过期
    public static boolean isTokenExpired(String token,JwtExpirationCallback callback) {
        Date expirationDate;
        try {
            expirationDate = getClaimsFromToken(token).getExpiration();
        } catch (Exception exception) {
            if (callback != null){
                callback.onJwtTokenExpired(token);
            }
            return true;
        }
        return expirationDate.before(new Date());
    }


    /**
     * 检查缓存数据中是否存在该 用户ID
     * @param userId
     * @return
     */
    public static boolean checkUid(String userId,String[] list){
        boolean checkUid = false;
        if (list != null){
            for (String uid : list){
                if (uid.equals(userId)){
                    checkUid = true;
                    break;
                }
            }
        }
        return checkUid;
    }
}

