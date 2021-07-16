package com.hackerrank.eshopping.product.dashboard.controller;

import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public HttpStatus addProduct(@RequestParam int id,
                                 @RequestParam String name,
                                 @RequestParam String category,
                                 @RequestParam double retail_price,
                                 @RequestParam double discounted_price,
                                 @RequestParam boolean availability){
        return productService.addProduct(id, name, category, retail_price, discounted_price, availability);
    }

    @PutMapping("/products/{product_id}")
    public HttpStatus updateProduct(@RequestParam int product_id,
                                    @RequestParam double retail_price,
                                    @RequestParam double discounted_price,
                                    @RequestParam boolean availability){
        return productService.updateProduct(product_id, retail_price, discounted_price, availability);
    }

    @GetMapping("/products/{product_id}")
    public @ResponseBody ResponseEntity <Product> findProductById(@RequestParam int id){
        return productService.findById(id);
    }

    @GetMapping("/products?category={category}")
    public @ResponseBody ResponseEntity<List<Product>> findProductByCategory(@RequestParam String category){
        return productService.findByCategory(category);
    }

    @GetMapping("/products?category={category}&availability= {availability}")
    public @ResponseBody ResponseEntity<List<Product>> findProductByCatAndAvail(@RequestParam String category, @RequestParam int availability){
        return productService.findByCatAndAvail(category, availability);
    }

    @GetMapping("/products")
    public @ResponseBody ResponseEntity<List<Product>> allProducts(){
        return productService.allProducts();
    }
}
