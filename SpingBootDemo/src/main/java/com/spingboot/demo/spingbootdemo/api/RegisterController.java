package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.RegisterBody;
import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.redis.RedisService;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/user")
public class RegisterController {
    private final UserService userService;

    private RedisService redisService;
    @Autowired
    public RegisterController(UserService userService,RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @Async
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<BaseResponse>> register(@RequestBody(required = false) RegisterBody registerBody) {
        if (registerBody == null ||registerBody.getName() == null || registerBody.getPassword() == null) {
            return ResponseUtils.asyncResponseError(Mark.ERROR_USER_LOGIN_CHECK,  Mark.ERROR_USER_LOGIN_CHECK, Mark.ERROR_USER_INFO);
        }

        if (registerBody.getName().length() < 6) {
            return ResponseUtils.asyncResponseError("用户名长度必须大于等于6个字符！", null, Mark.ERROR_USER_INFO);
        }

        if (registerBody.getPassword().length() < 8) {
            return ResponseUtils.asyncResponseError("密码长度必须大于等于8个字符！", null, Mark.ERROR_USER_INFO);
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByName(registerBody.getName()));
        if (user.isEmpty()) {
            User rgUser = new User();
            rgUser.setName(registerBody.getName());
            rgUser.setPassword(registerBody.getPassword());
            rgUser.setState(1);
            Optional<User> rgUserResponse = Optional.ofNullable(userService.register(rgUser));
            if (rgUserResponse.isPresent()) {
                redisService.addListValue(Mark.ALL_USER_DATA_KEY,rgUserResponse.get().getId().toString(),false);
                return ResponseUtils.asyncResponseSuccess("注册成功！",  rgUserResponse);
            }
        } else {
            return ResponseUtils.asyncResponseError("当前用户名称已被使用！", null, Mark.ERROR_USER_INFO);
        }

        return ResponseUtils.asyncResponseError("注册失败！", null, Mark.ERROR_USER_INFO);
    }
}
