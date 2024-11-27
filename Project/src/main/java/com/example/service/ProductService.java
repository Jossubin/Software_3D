package com.example.service;

import com.example.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getAllProductsSorted(String sortBy);
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    List<Product> getProductsByCategory(String category);
    List<Product> getNewProducts(int limit);
    List<Product> searchProducts(String keyword);
}