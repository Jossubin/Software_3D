package com.example;

import com.example.Member;
import com.example.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void register(Member member) {
        // 이메일 중복 체크 (필요한 경우)
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }
        
        memberRepository.save(member);
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        return member;
    }
}