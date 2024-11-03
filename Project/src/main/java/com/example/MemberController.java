package com.example;

import com.example.Member;
import com.example.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("member", new Member());
        return "register";
    }

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

    @GetMapping("/memberList")
    public String memberList(Model model){
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "memberList";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model){
        model.addAttribute("adminForm", new AdminForm());
        return "admin";
    }

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
}