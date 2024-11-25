package com.example.service;

import com.example.model.Inquiry;
import com.example.model.Member;

import java.util.List;

public interface InquiryService {
    List<Inquiry> getAllInquiries();
    List<Inquiry> getInquiriesByMember(Member member);
    Inquiry saveInquiry(Inquiry inquiry);
    void answerInquiry(Long id, String answer);
    Inquiry getInquiryById(Long id);
} 