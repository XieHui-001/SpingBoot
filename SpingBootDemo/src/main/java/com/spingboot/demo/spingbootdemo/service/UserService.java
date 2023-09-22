package com.spingboot.demo.spingbootdemo.service;

import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @param name 用户名称
     * @return 返回用户数据实体
     */
    public User getUserByName(String name){
        return userRepository.findUserByName(name);
    }

    public User register(User user){
        return userRepository.save(user);
    }
}