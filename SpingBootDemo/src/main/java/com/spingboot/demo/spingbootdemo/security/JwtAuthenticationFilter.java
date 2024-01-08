package com.spingboot.demo.spingbootdemo.security;

import com.google.gson.Gson;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.redis.RedisService;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.utils.ClientIpUtils;
import com.spingboot.demo.spingbootdemo.utils.JwtUtils;
import com.spingboot.demo.spingbootdemo.utils.RequestCounter;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomerUserDetailsService customerUserDetailsService;
    private final RedisService redisService;

    private final Integer maxRequestNumber = 20; // 每分钟请求最大数
    private static final ConcurrentHashMap<String, RequestCounter> REQUEST_COUNT_MAP = new ConcurrentHashMap<>(); // IP请求缓存池

    /***
     * 默认授权的接口地址
     */
    private final String[] hasDefaultPermissionsApi = new String[]{ "/api/login","/api/getToken","/api/user/register","/api/testDeleteUser" };

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String clientIP = ClientIpUtils.getClientIP(request);
        log.info("客户端请求IP地址:"+clientIP);

        RequestCounter requestCounter = REQUEST_COUNT_MAP.compute(clientIP, (k, v) -> {
            if (v == null || v.isExpired(60000)) { // 设置过期时间为60秒
                return new RequestCounter();
            } else {
                v.increment();
                return v;
            }
        });

        log.info("当前API接口请求次数:" + requestCounter.getRequestCount() + "   " + request.getServletPath());

        if (requestCounter.getRequestCount() > maxRequestNumber) {
            // 超过访问限制，返回限制提示
            sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError("The request is too frequent. Try again in one minute.","",Mark.ERROR_DEFAULT).getBody()));
            return;
        }


        for (String path : hasDefaultPermissionsApi){
            if (path.equals(request.getServletPath())){
                filterChain.doFilter(request,response);
                return;
            }
        }

        String token = JwtUtils.getToken(request);

        if (StringUtils.isEmpty(token)){
            sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError("Token is missing.","",Mark.ERROR_TOKEN_EXPIRES).getBody()));
            return ;
        }

        if (!JwtUtils.isTokenExpired(token)) {
            Claims claims = JwtUtils.getClaimsFromToken(token);
            String uid = claims.getSubject();
            if (!redisService.getList(Mark.ALL_USER_DATA_KEY).isEmpty()){
                List<Object> uidList =  redisService.getList(Mark.ALL_USER_DATA_KEY);
                for (Object id : uidList) {
                    if (id.toString().equals(uid)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(uid ,null , null);
                        log.info("authenticated user with uid :{}", uid);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request,response);
                        return;
                    }
                }
            }

            sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,"",Mark.ERROR_TOKEN_EXPIRES).getBody()));
//            if (redisService.getList(Mark.ALL_USER_DATA_KEY).isEmpty()){
//                UserDetails userDetails = customerUserDetailsService.loadUserByUsername(uid);
//                if (userDetails != null) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername() ,null , userDetails.getAuthorities());
//                    log.info("authenticated user with uid :{}", uid);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }else{
//                    sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,"",Mark.ERROR_TOKEN_EXPIRES).getBody()));
//                    return;
//                }
//            }else{
//                List<Object> uidList =  redisService.getList(Mark.ALL_USER_DATA_KEY);
//                for (Object id : uidList) {
//                    if (id.toString().equals(uid)) {
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername() ,null , userDetails.getAuthorities());
//                        log.info("authenticated user with uid :{}", uid);
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        filterChain.doFilter(request,response);
//                        return;
//                    }
//                }
//
//                sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,"",Mark.ERROR_TOKEN_EXPIRES).getBody()));
//                return;
//            }
        }else{
            sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,"",Mark.ERROR_TOKEN_EXPIRES).getBody()));
        }
    }


    private void sendTokenMissingResponse(HttpServletResponse response,String data) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(data);
        response.getWriter().flush();
    }

}
