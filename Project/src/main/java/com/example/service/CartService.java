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
} 