package com.example.service;

import com.example.model.Cart;
import com.example.model.Member;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.repository.CartRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void checkout(Member member) {
        // 회원의 장바구니 항목 조회
        List<Cart> cartItems = cartRepository.findByMember(member);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }

        // 주문 총액 계산
        Double subtotal = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Double shippingFee = 5.00; // 고정 배송비
        Double totalAmount = subtotal + shippingFee;

        // 주문 생성
        Order order = new Order();
        order.setMember(member);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("입금 대기");
        order.setTotalAmount(totalAmount);

        // 주문 항목 추가
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSelectedColor(cartItem.getSelectedColor());
            orderItem.setSelectedSize(cartItem.getSelectedSize());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
        }

        // 주문 저장 (OrderItem도 함께 저장)
        orderRepository.save(order);

        // 장바구니 비우기
        cartRepository.deleteByMember(member);
    }

    public List<Order> getRecentOrders(Member member) {
        return orderRepository.findByMemberIdOrderByOrderDateDesc(member.getId());
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
}