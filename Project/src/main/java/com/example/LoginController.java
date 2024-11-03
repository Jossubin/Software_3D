package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private MemberRepository memberRepository;

    // GET 요청 시 로그인 폼 표시
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // templates/login.html 렌더링
    }

    // POST 요청 시 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member != null && member.getPassword().equals(password)) { // 비밀번호 비교 (실습용, 실제로는 암호화 필요)
            // 로그인 성공: 세션에 사용자 정보 저장
            session.setAttribute("loggedInUser", member);
            return "redirect:/welcome"; // 로그인 후 welcome 페이지로 이동
        } else {
            // 로그인 실패: 오류 메시지와 함께 로그인 페이지로 이동
            model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login?logout"; // 로그아웃 후 로그인 페이지로 이동
    }

    // GET 요청 시 welcome 페이지 표시
    @GetMapping("/welcome")
    public String welcome(Model model, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("message", loggedInUser.getName() + "님이 접속하였습니다.");
        } else {
            model.addAttribute("message", "로그인하지 않았습니다.");
        }
        return "welcome"; // templates/welcome.html 렌더링
    }
}