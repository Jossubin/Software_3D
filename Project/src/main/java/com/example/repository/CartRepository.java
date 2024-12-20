package com.example.repository;

import com.example.model.Cart;
import com.example.model.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember(Member member);
    void deleteByMember(Member member);
} 