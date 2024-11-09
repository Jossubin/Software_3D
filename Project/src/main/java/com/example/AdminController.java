package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;


    @GetMapping("/admin")
    public String showAdminForm(Model model) {
        model.addAttribute("adminForm", new AdminForm());
        return "admin";
    }

    @PostMapping("/admin")
    public String updateInvoice(@ModelAttribute AdminForm adminForm, Model model) {
        Member member = memberRepository.findByEmail(adminForm.getEmail()).orElse(null);
        if (member != null) {
            member.setInvoice(adminForm.getInvoice());
            memberRepository.save(member);
            model.addAttribute("successMessage", "송장번호가 업데이트되었습니다.");
        } else {
            model.addAttribute("errorMessage", "해당 이메일의 회원을 찾을 수 없습니다.");
        }
        return "admin";
    }

    @GetMapping("/memberList")
    public String showMemberList(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "memberList";
    }
}