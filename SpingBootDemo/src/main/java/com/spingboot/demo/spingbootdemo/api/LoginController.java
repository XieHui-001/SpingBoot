package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.LoginBody;
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
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public <T> ResponseEntity<BaseResponse<T>> login(@RequestBody(required = false) LoginBody loginRequest) {
        if (loginRequest == null||loginRequest.getName() == null || loginRequest.getPassword() == null) {
            return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,null,Mark.ERROR_USER_INFO);
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByName(loginRequest.getName()));
        if (user.isPresent()){
            if (user.get().getPassword().compareTo(loginRequest.getPassword()) != 0) {
                return ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,null,Mark.ERROR_USER_INFO);
            }
        }else{
            return ResponseUtils.responseError("该账号没有创建！",null,Mark.ERROR_NOT_USER);
        }

        return ResponseUtils.responseSuccess("登录成功！",(T) user);
    }
}