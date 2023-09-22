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

    /**
     * 注册
     * @param user 需要注册的用户信息
     * @return
     */
    public User register(User user){
        return userRepository.save(user);
    }

    /**
     * 删除指定用户
     * @param id 用户ID
     */
    public void deleteUser(Long id){
         userRepository.deleteById(id);
    }
}