package com.spingboot.demo.spingbootdemo.repository;

import com.spingboot.demo.spingbootdemo.bean.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT u FROM Product u WHERE u.id > :id GROUP BY u.id")
    List<Product> selectProductByPage(@Param("id") int id, Pageable pageable);

    @Query("SELECT u FROM Product u where u.id =:id")
    Product queryProductById(@Param("id") Integer id);
}