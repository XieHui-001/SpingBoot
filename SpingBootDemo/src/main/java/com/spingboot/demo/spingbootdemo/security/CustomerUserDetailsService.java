package com.spingboot.demo.spingbootdemo.security;

import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // id都是由整数形成 判断是否是整数
        if (id.matches("-?\\d+")){
            Optional<User> user = iUserRepository.findById(Long.parseLong(id));
            if (user.isPresent()){
                return user.get();
            }
        }
        return null;
    }
}