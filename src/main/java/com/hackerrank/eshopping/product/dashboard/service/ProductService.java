package com.hackerrank.eshopping.product.dashboard.service;

import com.hackerrank.eshopping.product.dashboard.exception.BadRequest;
import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static String err;

    @Autowired
    private ProductRepository productRepository;

    public void save(List<Product> users) {

    }

    public boolean addProduct(int id, String name, String category, double retail_price, double discounted_price, boolean availability) {
        Product product = new Product(id, name, category, retail_price, discounted_price, availability);
        productRepository.save(product);
        if(productRepository.existsById(id)){
            return false;
        }
        return true;
    }

    public boolean updateProduct(int product_id, double retail_price, double discounted_price, boolean availability) {
        if(productRepository.existsById(product_id)){
            Product product = productRepository.getOne(product_id);
            product.setRetail_price(retail_price);
            product.setDiscounted_price(discounted_price);
            product.setAvailability(availability);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    // Testeando ResponseEntity, una vez lo logre cambiar toda la API con ResponseEntity
    public ResponseEntity<Product> findById(int id) {
        if(!exist(id)){
            throw new BadRequest( err );
        }
        Product product = productRepository.getOne(id);
        return new ResponseEntity<>(product, HttpStatus.valueOf(200));
    }

    private boolean exist(int id) {
        return productRepository.existsById(id);
    }
}
