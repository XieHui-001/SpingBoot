package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.BaseIdBody;
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
    public LoginController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    public <T> ResponseEntity<BaseResponse<T>> login(@RequestBody(required = false) LoginBody loginRequest, @RequestHeader(value = "token", required = false) String authToken) {
        if (loginRequest == null || loginRequest.getName() == null || loginRequest.getPassword() == null) {
            return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK, null, Mark.ERROR_USER_INFO);
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByName(loginRequest.getName()));
        if (user.isPresent()) {
            if (!user.get().getPassword().equals(loginRequest.getPassword())) {
                return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK, null, Mark.ERROR_USER_INFO);
            }
        } else {
            return ResponseUtils.responseError("该账号不存在！", null, Mark.ERROR_NOT_USER);
        }

        if (authToken == null || JwtUtils.getInstance().isTokenExpired(authToken,null)) {
            redisService.saveData(RedisConfig.REDIS_BASE_TOKEN_KEY + user.get().getId(), JwtUtils.getInstance().generateToken(user.get().getId().toString()));
        }else if (user.get().getState() == 1) {
            return ResponseUtils.responseError("该账号已登录,请勿重复登录!", null, Mark.ERROR_USER_INFO);
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.get().getName());
        userMap.put("uid", user.get().getId());
        userMap.put("state", user.get().getState());
        userMap.put("token", redisService.getData(RedisConfig.REDIS_BASE_TOKEN_KEY + user.get().getId()));

        userService.updateUserState(1, user.get().getId());

        return ResponseUtils.responseSuccess("登录成功！", (T) userMap);
    }

    @PostMapping("/singOut")
    public <T> ResponseEntity<BaseResponse<T>> singOut(@RequestBody(required = false) BaseIdBody body, @RequestHeader(value = "token", required = false) String token) {
        if (token == null || JwtUtils.getInstance().isTokenExpired(token,null)){
            return ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN, null, Mark.ERROR_TOKEN_EXPIRES);
        }

        if (body == null || body.getId() == null || body.getId() <= 0) {
            return ResponseUtils.responseError("用户ID 错误", null, Mark.ERROR_DEFAULT);
        }

        userService.updateUserState(0, body.getId().longValue());

        return ResponseUtils.responseSuccess("操作成功!",null);
    }


    @PostMapping("/deleteUser")
    public <T> ResponseEntity<BaseResponse<T>> deleteUser(@RequestBody(required = false) BaseIdBody body,@RequestHeader(value = "token",required = false) String token){
        if (token == null || JwtUtils.getInstance().isTokenExpired(token,null)){
            return ResponseUtils.responseError(Mark.ERROR_CHECK_TOKEN,null,Mark.ERROR_TOKEN_EXPIRES);
        }

        if (body == null || body.getId() == null || body.getId() <= 0){
            return ResponseUtils.responseError("用户ID错误",null,Mark.ERROR_DEFAULT);
        }

        Optional<User> delUser = userService.getUserById(body.getId());
        if (delUser.isPresent()){
            userService.deleteUser(delUser.get().getId());
            return ResponseUtils.responseSuccess("删除成功",null);
        }else{
            return ResponseUtils.responseError("该用户不存在",null,Mark.ERROR_DEFAULT);
        }
    }
}