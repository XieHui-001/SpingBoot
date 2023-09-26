package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.LoginBody;
import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.redis.RedisConfig;
import com.spingboot.demo.spingbootdemo.redis.RedisService;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.service.UserService;
import com.spingboot.demo.spingbootdemo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;
    private final RedisService redisService;
    @Autowired
    public LoginController(UserService userService,RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    public <T> ResponseEntity<BaseResponse<T>> login(@RequestBody(required = false) LoginBody loginRequest,@RequestHeader(value = "token", required = false) String authToken) {
        if (loginRequest == null||loginRequest.getName() == null || loginRequest.getPassword() == null) {
            return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,null,Mark.ERROR_USER_INFO);
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByName(loginRequest.getName()));
        if (user.isPresent()){
            if (user.get().getState() == 1){
                return ResponseUtils.responseError("该账号已登录,请勿重复登录!",null,Mark.ERROR_USER_INFO);
            }
            if (user.get().getPassword().compareTo(loginRequest.getPassword()) != 0) {
                return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,null,Mark.ERROR_USER_INFO);
            }
        }else{
            return ResponseUtils.responseError("该账号没有创建！",null,Mark.ERROR_NOT_USER);
        }
        if (authToken == null || JwtUtils.getInstance().isTokenExpired(authToken)){
            redisService.saveData(RedisConfig.REDIS_BASE_TOKEN_KEY + user.get().getId(),JwtUtils.getInstance().generateToken(user.get().getId().toString()));
        }

        Map<String,Object> userMap = new HashMap<>();
        userMap.put("name",user.get().getName());
        userMap.put("state",user.get().getState());
        userMap.put("token",redisService.getData(RedisConfig.REDIS_BASE_TOKEN_KEY + user.get().getId()));
        userService.updateUserState(1,user.get().getId());
        return ResponseUtils.responseSuccess("登录成功！",(T) userMap);
    }
}