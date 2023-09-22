package com.spingboot.demo.spingbootdemo.service;

import com.spingboot.demo.spingbootdemo.bean.Product;
import com.spingboot.demo.spingbootdemo.repository.ProductRepository;
import com.spingboot.demo.spingbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository userRepository) {
        this.productRepository = userRepository;
    }

    /**
     * 查找所有商品信息
     * @return
     */
    public List<Product> queryAllProduct() {
        return productRepository.selectAllProduct();
    }
}
