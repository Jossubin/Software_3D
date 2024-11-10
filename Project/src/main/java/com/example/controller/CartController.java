package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.service.CartService;
import com.example.Member;
import com.example.model.Cart;

import java.util.List;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        
        if (loginMember == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartItems(loginMember);
        model.addAttribute("cartItems", cartItems);
        
        return "cart";
    }
} 