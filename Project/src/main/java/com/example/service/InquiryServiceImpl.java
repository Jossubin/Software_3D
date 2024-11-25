package com.example.service;

import com.example.model.Inquiry;
import com.example.model.Member;
import com.example.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;

    @Override
    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAllByOrderByCreatedDateDesc();
    }

    @Override
    public List<Inquiry> getInquiriesByMember(Member member) {
        return inquiryRepository.findByMemberOrderByCreatedDateDesc(member);
    }

    @Override
    public Inquiry saveInquiry(Inquiry inquiry) {
        return inquiryRepository.save(inquiry);
    }

    @Override
    public void answerInquiry(Long id, String answer) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("문의를 찾을 수 없습니다."));
        
        inquiry.setAnswer(answer);
        inquiry.setAnswered(true);
        inquiry.setAnsweredDate(LocalDateTime.now());
        
        inquiryRepository.save(inquiry);
    }

    @Override
    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("문의를 찾을 수 없습니다."));
    }
} 