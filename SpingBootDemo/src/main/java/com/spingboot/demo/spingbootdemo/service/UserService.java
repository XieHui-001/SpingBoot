package com.spingboot.demo.spingbootdemo.service;

import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
     *
     * @param id 用户ID
     * @return 返回当前id 用户信息
     */
    public Optional<User> getUserById(Integer id){
        return userRepository.findById(id.longValue());
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

    /**
     *
     * @param state 用户状态
     * @param id 用户ID
     * @return 修改后的用户数据
     */
    public Integer updateUserState(Integer state,Long id){
        return userRepository.updateUserStateById(state,id);
    }
}