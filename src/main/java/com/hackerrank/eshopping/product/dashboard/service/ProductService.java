package com.hackerrank.eshopping.product.dashboard.service;

import com.hackerrank.eshopping.product.dashboard.exception.BadRequest;
import com.hackerrank.eshopping.product.dashboard.exception.NotFound;
import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static String err;

    @Autowired
    private ProductRepository productRepository;

    public HttpStatus addProduct(int id, String name, String category, double retail_price, double discounted_price, boolean availability) {
        if(exist(id)){
            throw new BadRequest(err);
        }
        Product product = new Product(id, name, category, retail_price, discounted_price, availability);
        productRepository.save(product);
        return HttpStatus.valueOf(201);
    }

    public HttpStatus updateProduct(int product_id, double retail_price, double discounted_price, boolean availability) {
        if(!exist(product_id)){
            throw new BadRequest(err);
        }
        Product product = productRepository.getOne(product_id);
        product.setRetail_price(retail_price);
        product.setDiscounted_price(discounted_price);
        product.setAvailability(availability);
        productRepository.save(product);
        return HttpStatus.valueOf(200);
    }

    public ResponseEntity<Product> findById(int id) {
        if(!exist(id)){
            throw new NotFound( err );
        }
        Product product = productRepository.getOne(id);
        return new ResponseEntity<>(product, HttpStatus.valueOf(200));
    }

    public ResponseEntity<List<Product>> findByCategory(String category) {
        if(!existCategory(category)){
            throw new NotFound( err );
        }
        List<Product> products = productRepository.findAll();
        List<Product> productsByCategory = products.stream().filter(product -> product.getCategory().equals(category)).collect(Collectors.toList());
        List<Product> productsSortedByAvailability = productsByCategory.stream().sorted(Comparator.comparing(Product::isAvailability)).collect(Collectors.toList());
        List<Product> productsSortedByDiscounted = productsSortedByAvailability.stream().sorted(Comparator.comparing(Product::getDiscounted_price)).collect(Collectors.toList());
        List<Product> productsSortedById = productsSortedByDiscounted.stream().sorted(Comparator.comparing(Product::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(productsSortedById, HttpStatus.valueOf(200));
    }

    public ResponseEntity<List<Product>> findByCatAndAvail(String category, int availability) {
        // Working on
    }

    private boolean existCategory(String category) {
        List<Product> products = productRepository.findAll();
        return products.stream().filter(product -> product.getCategory().equals(category)).count() > 0;
    }

    private boolean exist(int id) {
        return productRepository.existsById(id);
    }
}
