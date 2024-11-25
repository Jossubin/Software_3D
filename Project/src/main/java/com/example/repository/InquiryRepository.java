package com.example.repository;

import com.example.model.Inquiry;
import com.example.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByMemberOrderByCreatedDateDesc(Member member);
    List<Inquiry> findAllByOrderByCreatedDateDesc();
} 