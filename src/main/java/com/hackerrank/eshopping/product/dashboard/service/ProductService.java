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

    @Autowired
    private ProductRepository productRepository;

    private static final String err = "Error: ";
    private final List<Product> products = productRepository.findAll();

    // Methods for the Controller

    public HttpStatus addProduct(int id, String name, String category, double retail_price, double discounted_price, boolean availability) {
        if(exist(id)){
            throw new BadRequest( err );
        }
        Product product = new Product(id, name, category, retail_price, discounted_price, availability);
        productRepository.save(product);
        return HttpStatus.valueOf(201);
    }

    public HttpStatus updateProduct(int product_id, double retail_price, double discounted_price, boolean availability) {
        if(!exist(product_id)){
            throw new BadRequest( err );
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

        List<Product> productsByCategory = products.stream()
                .filter(product -> product.getCategory().equals(category))
                .collect(Collectors.toList());
        List<Product> productsSortedByAvailability = productsByCategory.stream()
                .sorted(Comparator.comparing(Product::isAvailability,Comparator.reverseOrder()))
                        .collect(Collectors.toList());
        List<Product> productsSorted = finalSort(productsSortedByAvailability);
        return new ResponseEntity<>(productsSorted, HttpStatus.valueOf(200));
    }

    public ResponseEntity<List<Product>> findByCatAndAvail(String category, int availability) {
        if(!existCategory(category)){
            throw new NotFound( err );
        } else if(availability == 0){
            List<Product> productsAvailFalse = products.stream()
                    .filter(product -> !product.isAvailability())
                    .collect(Collectors.toList());
            List<Product> productsCat = productsAvailFalse.stream()
                    .filter(product -> product.getCategory().equals(category))
                    .collect(Collectors.toList());
            List<Product> productsSorted = sortedByPercentage(productsCat);
            return new ResponseEntity<>(productsSorted, HttpStatus.valueOf(200));
        }
        List<Product> productsAvailTrue = products.stream()
                .filter(product -> product.isAvailability())
                .collect(Collectors.toList());
        List<Product> productsCat = productsAvailTrue.stream()
                .filter(product -> product.getCategory().equals(category))
                .collect(Collectors.toList());
        List<Product> productsSorted = sortedByPercentage(productsCat);
        return new ResponseEntity<>(productsSorted, HttpStatus.valueOf(200));
    }

    public ResponseEntity<List<Product>> allProducts() {
        List<Product> productsSorted = products.stream().sorted(Comparator.comparingInt(Product::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(productsSorted, HttpStatus.valueOf(200));
    }

    // Private methods

    private List<Product> sortedByPercentage(List<Product> productsList) {
        int firstPer, secondPer;
        Product a, b;
        for(int i = 0; i < productsList.size() - 1; i++){
            a = productsList.get(i);
            b = productsList.get(i+1);
            firstPer = DiscountedPercentage(a);
            secondPer = DiscountedPercentage(b);
            if(firstPer < secondPer){
                productsList.set(i, b);
                productsList.set(i+1, a);
            } if(firstPer == secondPer){
                if(a.getDiscounted_price() == b.getDiscounted_price()){
                    if(a.getId() > b.getId()){
                        productsList.set(i, b);
                        productsList.set(i+1, a);
                    }
                } else if(a.getDiscounted_price() > b.getDiscounted_price()){
                    productsList.set(i, b);
                    productsList.set(i+1, a);
                }
            }
        }
        return productsList;
    }

    private int DiscountedPercentage(Product p) {
        return (int) ((p.getRetail_price() - p.getDiscounted_price()) / (p.getRetail_price() * 100));
    }

    private List<Product> finalSort(List<Product> productsList) {
        Product aux;
        for(int i = 0; i < productsList.size() -1; i++){
            if(productsList.get(i).isAvailability() == productsList.get(i+1).isAvailability()){
                if(productsList.get(i).getDiscounted_price() == productsList.get(i+1).getDiscounted_price()){
                    if(productsList.get(i).getId() > productsList.get(i+1).getId()){
                        aux = productsList.get(i);
                        productsList.set(i, productsList.get(i+1));
                        productsList.set(i+1, aux);
                    }
                } else if(productsList.get(i).getDiscounted_price() > productsList.get(i+1).getDiscounted_price()){
                    aux = productsList.get(i);
                    productsList.set(i, productsList.get(i+1));
                    productsList.set(i+1, aux);
                }
            }
        }
        return productsList;
    }

    private boolean existCategory(String category) {
        return products.stream().anyMatch(product -> product.getCategory().equals(category));
    }

    private boolean exist(int id) {
        return productRepository.existsById(id);
    }

    // Method for testing (I guess)

    public void save(List<Product> users) {
        for (Product product : users) {
            productRepository.save(product);
        }
    }
}
