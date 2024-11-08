package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DatabaseInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!memberRepository.existsByEmail("admin@admin")) {
            Member admin = new Member();
            admin.setEmail("admin@admin");
            admin.setPassword("admin");
            admin.setName("Admin");
            admin.setAdmin(true);
            admin.setPhone("000-0000-0000");
            memberRepository.save(admin);
        }
    }
} 