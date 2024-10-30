package com.example;

import com.example.Member;
import com.example.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        try {
            memberService.register(member);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}