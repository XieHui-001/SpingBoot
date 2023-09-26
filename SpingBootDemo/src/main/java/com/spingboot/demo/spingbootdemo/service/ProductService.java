package com.spingboot.demo.spingbootdemo.service;

import com.spingboot.demo.spingbootdemo.bean.Product;
import com.spingboot.demo.spingbootdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 分页查找商品列表
     *
     * @return
     */
    public List<Product> queryAllProductByPage(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.selectProductByPage(id,pageable);
    }

    /**
     *
     * @param id 商品ID
     * @return 根据商品ID返回商品信息
     */
    public Product queryProductById(Integer id){
        return productRepository.queryProductById(id);
    }

    /**
     *
     * @param id 根据商品ID删除商品
     */
    public void deleteProductById(Long id){
         productRepository.deleteById(id);
    }
}
