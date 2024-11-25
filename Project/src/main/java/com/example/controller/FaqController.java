package com.example.controller;

import com.example.model.Faq;
import com.example.model.Member;
import com.example.service.FaqService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/about")
    public String faqList(Model model, HttpSession session) {
        model.addAttribute("faqs", faqService.getAllFaqs());
        
        // 관리자 체크
        Object loginMember = session.getAttribute("loginMember");
        if (loginMember != null) {
            String email = ((Member) loginMember).getEmail();
            model.addAttribute("isAdmin", "admin@admin".equals(email));
        }
        
        return "faq/list";
    }

    @GetMapping("/about/write")
    public String faqWriteForm(Model model, HttpSession session) {
        // 관리자 체크
        Object loginMember = session.getAttribute("loginMember");
        if (loginMember == null || 
            !((Member) loginMember).getEmail().equals("admin@admin")) {
            return "redirect:/about";
        }
        
        model.addAttribute("faq", new Faq());
        return "faq/write";
    }

    @PostMapping("/about/write")
    public String faqWrite(@ModelAttribute Faq faq, HttpSession session) {
        // 관리자 체크
        Object loginMember = session.getAttribute("loginMember");
        if (loginMember == null || 
            !((Member) loginMember).getEmail().equals("admin@admin")) {
            return "redirect:/about";
        }
        
        faqService.saveFaq(faq);
        return "redirect:/about";
    }
} 