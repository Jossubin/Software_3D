package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.dto.CartRequest;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.model.Cart;
import com.example.model.Member;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
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

    // 새로운 Checkout 엔드포인트 추가
    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            orderService.checkout(loginMember);
            redirectAttributes.addFlashAttribute("success", "주문이 성공적으로 완료되었습니다.");
            return "redirect:/mypage";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "주문 처리 중 오류가 발생했습니다.");
            return "redirect:/cart";
        }
    }
}