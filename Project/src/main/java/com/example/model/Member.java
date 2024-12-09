package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "member") // 테이블 이름을 정확히 매칭 (CREATE TABLE은 'member')
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private String invoice;

    @Column
    private String profileImagePath;

    private boolean isAdmin;

    // 새로 추가된 필드들
    @Column(nullable = false)
    private Integer couponCount = 0;

    @Column(nullable = false)
    private Integer deliveredCount = 0;

    @Column(nullable = false)
    private Integer inTransitCount = 0;

    @Column(nullable = false)
    private Integer paymentCompletedCount = 0;

    @Column(nullable = false)
    private Integer pendingPaymentCount = 0;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(nullable = false)
    private Integer preparingShipmentCount = 0;

    @Column(nullable = false)
    private Integer reviewCount = 0;

    @Column(nullable = false)
    private boolean vip = false;

    @Column(name = "accumulated_amount", nullable = false)
    private Integer accumulatedAmount = 0;
    // 기본 생성자
    public Member() {
        this.id = null;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}