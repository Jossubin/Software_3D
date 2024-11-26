package com.example.controller;

import com.example.model.Order;
import com.example.model.Member;
import com.example.service.OrderService;
import com.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/order/details/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model, HttpSession session) {
        // 세션에서 로그인한 회원 정보 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        // 주문 정보 가져오기
        Optional<Order> optionalOrder = orderService.findById(orderId);
        Order order = optionalOrder.orElse(null);

        if (order == null || !order.getMember().getId().equals(loginMember.getId())) {
            // 주문이 존재하지 않거나, 해당 주문이 로그인한 회원의 것이 아님
            return "redirect:/mypage?error=invalidOrder";
        }

        // 배송 조회에 필요한 정보 모델에 추가
        model.addAttribute("order", order);
        model.addAttribute("member", loginMember);

        return "delivery-tracking"; // 배송 조회 템플릿 이름
    }
}