package com.example;

import com.example.Member;
import com.example.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    @Transactional
    public void register(Member member) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        
        // 비밀번호 암호화 로직은 나중에 추가 가능
        
        memberRepository.save(member);
    }
}