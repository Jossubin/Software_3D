package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.example.service.OrderService;
import com.example.model.Member;
import com.example.model.Order;

import java.util.List;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    private final OrderService orderService;

    public MypageController(OrderService orderService) {
        this.orderService = orderService;
    }


}