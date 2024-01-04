package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.body.BaseIdBody;
import com.spingboot.demo.spingbootdemo.body.BasePageBody;
import com.spingboot.demo.spingbootdemo.bean.Product;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.service.ProductService;
import com.spingboot.demo.spingbootdemo.service.UserService;
import com.spingboot.demo.spingbootdemo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService,UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/query")
    public  ResponseEntity<BaseResponse> queryAllProduct(@RequestBody(required = false) BasePageBody pageBody) {

        if (pageBody == null || pageBody.getId() == null || pageBody.getSize() == null || pageBody.getSize() <= 0) {
            return ResponseUtils.responseError("基础分页参数错误", null, Mark.ERROR_DEFAULT);
        }

        Optional<List<Product>> productDataOptional = Optional.ofNullable(productService.queryAllProductByPage(pageBody.getId(), 0, pageBody.getSize()));
        Map<String, Object> map = new HashMap<>();
        map.put("time", new Date().getTime());
        map.put("list", productDataOptional.orElse(Collections.emptyList()));
        map.put("size", productDataOptional.map(List::size).orElse(0));

        return ResponseUtils.responseSuccess("查询成功", map);
    }

    @PostMapping("/deleteById")
    public  ResponseEntity<BaseResponse> deleteProductById(@RequestBody(required = false) BaseIdBody body){
        if (body == null || body.getId() == null){
            return ResponseUtils.responseError("操作失败Id不能为空",null,Mark.ERROR_DEFAULT);
        }

        Optional<Product> product = Optional.ofNullable(productService.queryProductById(body.getId()));
        if (product.isPresent()){
            productService.deleteProductById(product.get().getId());
            return ResponseUtils.responseSuccess("删除成功",null);
        }else{
            return ResponseUtils.responseError("该商品不存在",null,Mark.ERROR_DEFAULT);
        }
    }
}
