package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;           // 문의 제목
    private String content;         // 문의 내용
    private String answer;          // 답변 내용
    private boolean answered;       // 답변 상태
    private LocalDateTime createdDate; // 문의 작성일
    private LocalDateTime answeredDate; // 답변 작성일
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;          // 문의 작성자
    
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
} 