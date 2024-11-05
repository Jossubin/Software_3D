package com.example;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter @Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "address_name")
    private String addressName;
    
    @Column(name = "recipient")
    private String recipient;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    @Column(name = "main_address")
    private String mainAddress;
    
    @Column(name = "detail_address")
    private String detailAddress;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
} 