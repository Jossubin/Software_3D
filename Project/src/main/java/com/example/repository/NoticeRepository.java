package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
} 