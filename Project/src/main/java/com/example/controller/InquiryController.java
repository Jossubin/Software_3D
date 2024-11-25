package com.example.controller;

import com.example.model.Inquiry;
import com.example.model.Member;
import com.example.service.InquiryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    public String listInquiries(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        List<Inquiry> inquiries;
        if ("admin@admin".equals(loginMember.getEmail())) {
            // 관리자는 모든 문의를 볼 수 있음
            inquiries = inquiryService.getAllInquiries();
        } else {
            // 일반 사용자는 자신의 문의만 볼 수 있음
            inquiries = inquiryService.getInquiriesByMember(loginMember);
        }
        
        model.addAttribute("inquiries", inquiries);
        return "inquiry/list";
    }

    @GetMapping("/inquiry/write")
    public String showWriteForm(Model model) {
        model.addAttribute("inquiry", new Inquiry());
        return "inquiry/write";
    }

    @PostMapping("/inquiry/write")
    public String writeInquiry(@ModelAttribute Inquiry inquiry, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }
        
        inquiry.setMember(loginMember);
        inquiryService.saveInquiry(inquiry);
        return "redirect:/inquiry";
    }

    @PostMapping("/inquiry/answer/{id}")
    public String answerInquiry(@PathVariable Long id, @RequestParam String answer, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || !"admin@admin".equals(loginMember.getEmail())) {
            return "redirect:/inquiry";
        }

        inquiryService.answerInquiry(id, answer);
        return "redirect:/inquiry";  // 답변 후 문의 목록 페이지로 리다이렉트
    }
} 