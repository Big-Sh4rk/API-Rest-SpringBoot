package com.hackerrank.eshopping.product.dashboard.controller;

import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public HttpStatus addProduct(@RequestParam int id, @RequestParam String name, @RequestParam String category, @RequestParam double retail_price, @RequestParam double discounted_price, @RequestParam boolean availability){
        if(productService.addProduct(id, name, category, retail_price, discounted_price, availability)){
            return HttpStatus.valueOf(201);
        }
        return HttpStatus.valueOf(400);
    }

    @PutMapping("/products/{product_id}")
    public HttpStatus updateProduct(@RequestParam int product_id, @RequestParam double retail_price, @RequestParam double discounted_price, @RequestParam boolean availability){
        if(productService.updateProduct(product_id, retail_price, discounted_price, availability)){
            return HttpStatus.valueOf(200);
        }
        return HttpStatus.valueOf(400);
    }

    @GetMapping("/products/{product_id}")
    public @ResponseBody ResponseEntity <Product> findProductById(@RequestParam int id){
        return productService.findById(id);
    }

    /*@GetMapping("/products?category={category}")
    public*/
}
