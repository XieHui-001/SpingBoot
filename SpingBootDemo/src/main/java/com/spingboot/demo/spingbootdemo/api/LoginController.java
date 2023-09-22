package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.bean.LoginBean;
import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginBean loginRequest) {
        if (loginRequest.getName().isEmpty() || loginRequest.getPassword().isEmpty()) {
            BaseResponse response = ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,false,Mark.ERROR_USER_INFO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        User user = userService.getUserByName(loginRequest.getName());
        if (user != null){
            if (user.getPassword().compareTo(loginRequest.getPassword()) != 0) {
                BaseResponse response = ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK,false,Mark.ERROR_USER_INFO);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else{
            BaseResponse response = ResponseUtils.responseError("该账号没有创建！",false,Mark.ERROR_NOT_USER);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        BaseResponse response = ResponseUtils.responseSuccess("登录成功！",user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}