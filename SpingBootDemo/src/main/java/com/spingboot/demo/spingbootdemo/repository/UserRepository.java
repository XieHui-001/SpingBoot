package com.spingboot.demo.spingbootdemo.repository;

import com.spingboot.demo.spingbootdemo.bean.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.name = :name")
    User findUserByName(@Param("name") String name);
}