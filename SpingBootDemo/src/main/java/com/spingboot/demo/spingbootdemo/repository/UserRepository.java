package com.spingboot.demo.spingbootdemo.repository;

import com.spingboot.demo.spingbootdemo.bean.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.name = :name")
    User findUserByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.state =:state WHERE u.id =:id")
    void updateUserStateById(@Param("state") Integer state, @Param("id") Long id);

    @Query("SELECT u.id FROM User  u WHERE u.state = 1 ORDER BY u.id")
    List<Integer> findAllUser();
}