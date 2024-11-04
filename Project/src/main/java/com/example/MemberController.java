package com.example;

import com.example.Member;
import com.example.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final HttpSession session;

    // 회원 가입 폼 표시
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("member", new Member());
        return "register";
    }

    // 회원 가입 처리
    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes){
        try {
            memberService.register(member);
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 성공적으로 완료되었습니다!");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    // 로그인 폼 표시
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("successMessage", "로그아웃 되었습니다.");
        }
        return "login";  // login.html 템플릿을 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        try {
            Member authenticatedMember = memberService.login(email, password);
            // 로그인 성공 시 세션에 회원 정보 저장
            session.setAttribute("loggedInUser", authenticatedMember);
            redirectAttributes.addFlashAttribute("successMessage", "로그인에 성공하였습니다.");
            return "redirect:/";  // 메인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/login";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        session.invalidate(); // 세션 무효화
        redirectAttributes.addFlashAttribute("successMessage", "로그아웃 되었습니다.");
        return "redirect:/login"; // 로그아웃 후 로그인 페이지로 이동
    }

    // 홈 페이지
    @GetMapping("/")
    public String home(Model model) {
        // 로그인된 회원 정보를 가져옴
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("member", loggedInUser);
        }
        return "home";  // home.html 템플릿을 반환
    }

    // 회원 목록 조회 (관리자 기능)
    @GetMapping("/memberList")
    public String memberList(Model model){
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "memberList";
    }

    // 관리자 페이지 표시
    @GetMapping("/admin")
    public String showAdminPage(Model model){
        model.addAttribute("adminForm", new AdminForm());
        return "admin";
    }

    // 송장번호 업데이트 처리 (관리자 기능)
    @PostMapping("/admin")
    public String updateInvoice(@ModelAttribute AdminForm adminForm, RedirectAttributes redirectAttributes){
        Long memberId = adminForm.getId();
        String invoice = adminForm.getInvoice();

        Optional<Member> optionalMember = memberService.findById(memberId);
        if(optionalMember.isPresent()){
            Member member = optionalMember.get();
            member.setInvoice(invoice);
            memberService.save(member);
            redirectAttributes.addFlashAttribute("successMessage", "송장번호가 성공적으로 업데이트되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 ID를 가진 회원을 찾을 수 없습니다.");
        }

        return "redirect:/admin";
    }

    // 마이 페이지 표시
    @GetMapping("/mypage")
    public String myPage(Model model, RedirectAttributes redirectAttributes) {
        // 로그인된 회원 정보를 가져옴
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Long id = loggedInUser.getId();
            Optional<Member> memberOpt = memberService.findById(id);
            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                model.addAttribute("member", member);
                return "mypage"; // mypage.html 템플릿을 반환
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
                return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login";
        }
    }
}