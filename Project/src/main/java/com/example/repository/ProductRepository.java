// src/main/java/com/example/repository/ProductRepository.java
package com.example.repository;

import com.example.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 기존 메서드들
    List<Product> findByCategory(String category);


    List<Product> findByIsNewTrueOrderByCreatedDateDesc(Pageable pageable);
}