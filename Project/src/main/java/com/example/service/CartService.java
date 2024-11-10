package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.repository.CartRepository;
import com.example.Member;
import com.example.model.Product;
import com.example.MemberRepository;
import com.example.repository.ProductRepository;
import com.example.model.Cart;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addToCart(Long productId, int quantity, String size, String color, String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        Cart cart = new Cart();
        cart.setMember(member);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setSize(size);
        cart.setColor(color);

        cartRepository.save(cart);
    }
} 