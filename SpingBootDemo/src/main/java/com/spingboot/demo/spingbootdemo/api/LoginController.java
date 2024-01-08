package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.BaseIdBody;
import com.spingboot.demo.spingbootdemo.body.LoginBody;
import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.redis.RedisService;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.service.UserService;
import com.spingboot.demo.spingbootdemo.utils.Base64Utils;
import com.spingboot.demo.spingbootdemo.utils.JwtUtils;
import com.spingboot.demo.spingbootdemo.utils.Sha256Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;
    public final RedisService redisService;


    @Autowired
    public LoginController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @Async
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<BaseResponse>> login(@RequestBody(required = false) LoginBody loginBody) {
        if (loginBody == null || loginBody.getName() == null || loginBody.getPassword() == null || loginBody.getMark() == null) {
            return ResponseUtils.asyncResponseError(Mark.ERROR_USER_PARAMETER, null, Mark.ERROR_USER_INFO);
        }
        String loginMark = loginBody.getName() + loginBody.getPassword() + Mark.LOGIN_USER_CHECK_MARK;
        if (!Sha256Utils.checkLogin(loginMark,loginBody.getMark())){
            return ResponseUtils.asyncResponseError("登录信息验证失败！", null, Mark.ERROR_BASE);
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByName(Base64Utils.encrypt(loginBody.getName())));
        if (user.isPresent()) {
            if (!user.get().getPassword().equals(Base64Utils.encrypt(loginBody.getPassword()))) {
                return ResponseUtils.asyncResponseError(Mark.ERROR_USER_LOGIN_CHECK, null, Mark.ERROR_USER_INFO);
            }
        } else {
            return ResponseUtils.asyncResponseError("该账号不存在！", null, Mark.ERROR_NOT_USER);
        }

        String token = JwtUtils.generateToken(user.get().getId().toString());
        userService.updateUserToken(user.get().getId(),token);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.get().getName());
        userMap.put("uid", user.get().getId());
        userMap.put("token", token);


        return ResponseUtils.asyncResponseSuccess("登录成功！",  userMap);
    }

    @PostMapping("/singOut")
    public  ResponseEntity<BaseResponse> singOut(@RequestBody(required = false) BaseIdBody body) {
        if (body == null || body.getId() == null || body.getId() <= 0) {
            return ResponseUtils.responseError("用户ID 错误", null, Mark.ERROR_DEFAULT);
        }

        userService.deleteUserToken(body.getId().longValue());

        return ResponseUtils.responseSuccess("操作成功!",null);
    }


    @PostMapping("/deleteUser")
    public  ResponseEntity<BaseResponse> deleteUser(@RequestBody(required = false) BaseIdBody body){
        if (body == null || body.getId() == null || body.getId() <= 0){
            return ResponseUtils.responseError("用户ID错误",null,Mark.ERROR_DEFAULT);
        }

        Optional<User> delUser = userService.getUserById(body.getId());
        if (delUser.isPresent()){
            userService.deleteUser(delUser.get().getId());
            redisService.remove(Mark.ALL_USER_DATA_KEY,delUser.get().getId().toString());
            return ResponseUtils.responseSuccess("删除成功",null);
        }else{
            return ResponseUtils.responseError("该用户不存在",null,Mark.ERROR_DEFAULT);
        }
    }

    @PostMapping("/testDeleteUser")
    public  ResponseEntity<BaseResponse> testDeleteUser(@RequestBody(required = false) BaseIdBody body){
        if (body == null || body.getId() == null || body.getId() <= 0){
            return ResponseUtils.responseError("用户ID错误",null,Mark.ERROR_DEFAULT);
        }

        Optional<User> delUser = userService.getUserById(body.getId());
        if (delUser.isPresent()){
            userService.deleteUser(delUser.get().getId());
            redisService.remove(Mark.ALL_USER_DATA_KEY,delUser.get().getId().toString());
            return ResponseUtils.responseSuccess("删除成功",null);
        }else{
            return ResponseUtils.responseError("该用户不存在",null,Mark.ERROR_DEFAULT);
        }
    }


    @PostMapping("/getToken")
    public Object getToken(@RequestBody(required = false) BaseIdBody body){
        List<Object> uidList =  redisService.getList(Mark.ALL_USER_DATA_KEY);
        boolean hasUser = false;
        for (Object uid : uidList) {
            if (uid.toString().equals(body.getId().toString())) {
                hasUser = true;
                break;
            }
        }

        return  hasUser ? ResponseUtils.responseSuccess("获取token成功",JwtUtils.generateToken(body.getId().toString())) : ResponseUtils.responseError("用户不存在",null,Mark.ERROR_BASE);
    }
}