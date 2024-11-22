package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.dto.CartRequest;

import com.example.service.CartService;
import com.example.model.Cart;
import com.example.model.Member;

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

    @DeleteMapping("/cart/remove/{itemId}")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(@PathVariable("itemId") Long itemId, HttpSession session) {
        try {
            Member loginMember = (Member) session.getAttribute("loginMember");
            if (loginMember == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            cartService.removeCartItem(itemId, loginMember.getId());
            return ResponseEntity.ok().build();
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(@RequestBody CartRequest request, HttpSession session) {
        try {
            Member loginMember = (Member) session.getAttribute("loginMember");
            if (loginMember == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            cartService.addToCart(
                loginMember,
                request.getProductId(),
                request.getQuantity(),
                request.getSize(),
                request.getColor()
            );

            return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("장바구니 추가 중 오류가 발생했습니다.");
        }
    }
} 