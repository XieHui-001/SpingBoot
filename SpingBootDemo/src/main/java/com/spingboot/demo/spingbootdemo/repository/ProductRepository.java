package com.spingboot.demo.spingbootdemo.repository;

import com.spingboot.demo.spingbootdemo.bean.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query("SELECT u FROM Product u ")
    List<Product> selectAllProduct();
}