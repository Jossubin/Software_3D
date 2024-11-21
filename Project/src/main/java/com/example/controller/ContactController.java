package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import com.example.repository.MemberRepository;
import com.example.model.Member;
import com.example.model.Notice;
import com.example.service.NoticeService;

import java.util.List;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {
    
    private final NoticeService noticeService;
    private final MemberRepository memberRepository;
    
    @GetMapping("/notice")
    public String notice(Model model, HttpSession session) {
        List<Notice> notices = noticeService.getAllNotices();
        
        Long memberId = (Long) session.getAttribute("memberId");
        boolean isAdmin = false;
        if (memberId != null) {
            Member member = memberRepository.findById(memberId).orElse(null);
            isAdmin = member != null && member.isAdmin();
        }
        
        model.addAttribute("notices", notices);
        model.addAttribute("isAdmin", isAdmin);
        return "contact/notice";
    }
    
    @GetMapping("/notice/write")
    public String noticeWriteForm(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || !"admin@admin".equals(loginMember.getEmail())) {
            return "redirect:/contact/notice";
        }
        return "contact/notice-write";
    }
    
    @PostMapping("/notice/write")
    public String noticeWrite(@ModelAttribute Notice notice, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || !"admin@admin".equals(loginMember.getEmail())) {
            return "redirect:/contact/notice";
        }
        
        notice.setWriter("관리자");
        notice.setCreatedDate(LocalDateTime.now());
        notice.setViewCount(0);
        
        noticeService.saveNotice(notice);
        return "redirect:/contact/notice";
    }
    
    @GetMapping("/inquiry")
    public String inquiry(Model model) {
        return "contact/inquiry";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        return "contact/about";
    }
    
    @GetMapping("/notice/detail/{id}")
    public String noticeDetail(@PathVariable("id") Long id, Model model) {
        Notice notice = noticeService.getNotice(id);
        System.out.println("Controller received notice: " + notice);
        
        if (notice == null) {
            return "redirect:/contact/notice";
        }
        
        noticeService.increaseViewCount(id);
        model.addAttribute("notice", notice);
        return "contact/notice-detail";
    }
    
    @GetMapping("/notice/delete/{id}")
    public String deleteNotice(@PathVariable("id") Long id, HttpSession session) {
        // admin 권한 체크
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || !"admin@admin".equals(loginMember.getEmail())) {
            return "redirect:/contact/notice";
        }
        
        noticeService.deleteNotice(id);
        return "redirect:/contact/notice";
    }
    
    @GetMapping("/notice/edit/{id}")
    public String editNoticeForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        // admin 권한 체크
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || !"admin@admin".equals(loginMember.getEmail())) {
            return "redirect:/contact/notice";
        }
        
        Notice notice = noticeService.getNotice(id);
        if (notice == null) {
            return "redirect:/contact/notice";
        }
        
        model.addAttribute("notice", notice);
        return "contact/notice-edit";
    }
    
    @PostMapping("/notice/edit/{id}")
    public String editNotice(@PathVariable("id") Long id, @ModelAttribute Notice notice, HttpSession session) {
        // admin 권한 체크
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null || !"admin@admin".equals(loginMember.getEmail())) {
            return "redirect:/contact/notice";
        }
        
        noticeService.updateNotice(id, notice);
        return "redirect:/contact/notice";
    }
} 