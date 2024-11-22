package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.repository.CartRepository;
import com.example.repository.MemberRepository;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.model.Cart;
import com.example.model.Member;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addToCart(Member member, Long productId, int quantity, 
                         String selectedSize, String selectedColor) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다"));

        Cart cart = new Cart();
        cart.setMember(member);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setSelectedSize(selectedSize);
        cart.setSelectedColor(selectedColor);

        cartRepository.save(cart);
    }

    public List<Cart> getCartItems(Member member) {
        return cartRepository.findByMember(member);
    }

    @Transactional
    public void removeCartItem(Long cartItemId, Long memberId) {
        Cart cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다"));
        
        // 본인의 장바구니 아이템인지 확인
        if (!cartItem.getMember().getId().equals(memberId)) {
            throw new RuntimeException("권한이 없습니다");
        }
        
        cartRepository.delete(cartItem);
    }
    
    // 장바구니 전체 비우기 기능 (필요한 경우)
    @Transactional
    public void clearCart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));
        
        cartRepository.deleteByMember(member);
    }
} 