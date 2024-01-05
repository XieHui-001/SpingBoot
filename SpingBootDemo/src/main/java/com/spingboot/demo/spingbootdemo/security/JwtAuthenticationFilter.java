package com.spingboot.demo.spingbootdemo.security;

import com.google.gson.Gson;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.utils.ClientIpUtils;
import com.spingboot.demo.spingbootdemo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
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


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomerUserDetailsService customerUserDetailsService;
    /***
     * 默认授权的接口地址
     */
    private final String[] adminApiPath = new String[]{"/api/login","/api/getToken","/api/user/register"};

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("客户端请求IP地址:"+ClientIpUtils.getClientIP(request));
        for (String path : adminApiPath){
            if (path.equals(request.getServletPath())){
                filterChain.doFilter(request,response);
                return;
            }
        }

        String token = JwtUtils.getToken(request);

        if (token == null){
            sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError("Token is missing.","",Mark.ERROR_TOKEN_EXPIRES).getBody()));
            return ;
        }

        if (!JwtUtils.isTokenExpired(token)) {
            Claims claims = JwtUtils.getClaimsFromToken(token);
            String uid = claims.getSubject();
            UserDetails userDetails = customerUserDetailsService.loadUserByUsername(uid);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername() ,null , userDetails.getAuthorities());
                log.info("authenticated user with uid :{}", uid);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,"",Mark.ERROR_TOKEN_EXPIRES).getBody()));
                return;
            }
        }else{
            sendTokenMissingResponse(response,new Gson().toJson(ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,"",Mark.ERROR_TOKEN_EXPIRES).getBody()));
            return;
        }
        filterChain.doFilter(request,response);
    }


    private void sendTokenMissingResponse(HttpServletResponse response,String data) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(data);
        response.getWriter().flush();
    }

}
