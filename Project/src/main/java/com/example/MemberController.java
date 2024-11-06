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
    public String register(@ModelAttribute Member member, Model model, RedirectAttributes redirectAttributes) {
        // 이메일 중복 체크
        if (memberService.isEmailExists(member.getEmail())) {
            model.addAttribute("emailError", true);
            return "register";
        }

        try {
            memberService.register(member);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/login";
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

    @GetMapping("/member/update")
    public String editProfileForm(Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("member", loginMember);
        return "edit-profile";
    }

    @PostMapping("/member/update")
    public String editProfile(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            member.setId(loginMember.getId()); // 기존 회원의 ID 설정
            memberService.updateMember(member);
            session.setAttribute("loginMember", member); // 세션 정보 업데이트
            redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었습니다.");
            return "redirect:/mypage";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/update";
        }
    }

    @PostMapping("/member/delete")
    public String deleteMember(HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            System.out.println("회원 탈퇴 요청: " + loginMember.getEmail());
            memberService.deleteMember(loginMember.getId());

            // 세션 무효화
            session.invalidate();

            redirectAttributes.addFlashAttribute("message", "회원탈퇴가 완료되었습니다.");
            return "redirect:/";

        } catch (Exception e) {
            System.out.println("회원 탈퇴 실패: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "회원탈퇴 처리 중 오류가 발생했습니다.");
            return "redirect:/member/update";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션 무효화
        session.invalidate();
        
        redirectAttributes.addFlashAttribute("message", "로그아웃 되었습니다.");
        return "redirect:/";
    }
}