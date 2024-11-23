package com.example.repository;

import com.example.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // 필요한 경우 추가 메서드 정의
}