package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.bean.RegisterBean;
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
@RequestMapping("/api/user")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterBean registerBean) {

        if (registerBean.getName() == null || registerBean.getPassword() == null) {
            BaseResponse response = ResponseUtils.responseError(Mark.ERROR_USER_LOGIN_CHECK, Mark.ERROR_USER_LOGIN_CHECK, Mark.ERROR_USER_INFO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (registerBean.getName().length() < 6) {
            BaseResponse response = ResponseUtils.responseError("用户名长度必须大于等于6个字符！", false, Mark.ERROR_USER_INFO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (registerBean.getPassword().length() < 8) {
            BaseResponse response = ResponseUtils.responseError("密码长度必须大于等于8个字符！", false, Mark.ERROR_USER_INFO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        User user = userService.getUserByName(registerBean.getName());
        if (user == null) {
            User rgUser = new User();
            rgUser.setName(registerBean.getName());
            rgUser.setPassword(registerBean.getPassword());
            User rgUserResponse = userService.register(rgUser);
            if (rgUserResponse.getId() != null) {
                BaseResponse response = ResponseUtils.responseSuccess("注册成功！", rgUserResponse);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            BaseResponse response = ResponseUtils.responseError("当前用户名称已被使用！", false, Mark.ERROR_USER_INFO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
    }
}
