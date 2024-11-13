package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
