package com.example;

import com.example.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}