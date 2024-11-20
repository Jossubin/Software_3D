package com.example.repository;

import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 기본적으로 findAll(Sort sort)를 사용할 수 있지만, 필요한 경우 커스텀 메소드 추가 가능
}