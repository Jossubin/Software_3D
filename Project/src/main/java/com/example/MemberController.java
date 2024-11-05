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
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    private final HttpSession session;
    
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
    
    @GetMapping("/login")
    public String loginForm() {
        return "login";  // login.html 템플릿을 반환
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        try {
            Member authenticatedMember = memberService.login(member.getEmail(), member.getPassword());
            // 로그인 성공 시 세션에 회원 정보 저장
            session.setAttribute("loginMember", authenticatedMember);
            return "redirect:/";  // 메인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/login";
        }
    }
    
    @GetMapping("/")
    public String home(Model model) {
        // 로그인된 회원 정보를 가져옴
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember != null) {
            model.addAttribute("member", loginMember);
        }
        return "home";  // home.html 템플릿을 반환
    }
    
    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        // 세션에서 로그인된 회원 정보 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");
        
        // 로그인 체크
        if (loginMember == null) {
            return "redirect:/login";
        }
        
        // 모델에 회원 정보 추가
        model.addAttribute("member", loginMember);
        return "mypage";
    }
}