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
}