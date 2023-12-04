package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.RegisterBody;
import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class RegisterController {
    private final UserService userService;
    @Autowired
    public RegisterController(UserService userService) {this.userService = userService;}

    @PostMapping("/register")
    public  ResponseEntity<BaseResponse> register(@RequestBody(required = false) RegisterBody registerBody) {
        if (registerBody == null ||registerBody.getName() == null || registerBody.getPassword() == null) {
            return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,  Mark.ERROR_USER_LOGIN_CHECK, Mark.ERROR_USER_INFO);
        }

        if (registerBody.getName().length() < 6) {
            return ResponseUtils.responseError("用户名长度必须大于等于6个字符！", null, Mark.ERROR_USER_INFO);
        }

        if (registerBody.getPassword().length() < 8) {
            return ResponseUtils.responseError("密码长度必须大于等于8个字符！", null, Mark.ERROR_USER_INFO);
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByName(registerBody.getName()));
        if (user.isEmpty()) {
            User rgUser = new User();
            rgUser.setName(registerBody.getName());
            rgUser.setPassword(registerBody.getPassword());
            rgUser.setState(0);
            Optional<User> rgUserResponse = Optional.ofNullable(userService.register(rgUser));
            if (rgUserResponse.isPresent()) {
                return ResponseUtils.responseSuccess("注册成功！",  rgUserResponse);
            }
        } else {
            return ResponseUtils.responseError("当前用户名称已被使用！", null, Mark.ERROR_USER_INFO);
        }

        return ResponseUtils.responseError("注册失败！", null, Mark.ERROR_USER_INFO);
    }
}
