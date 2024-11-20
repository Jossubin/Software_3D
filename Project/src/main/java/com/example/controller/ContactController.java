package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ContactController {
    
    @GetMapping("/notice")
    public String notice(Model model) {
        return "contact/notice";
    }
    
    @GetMapping("/inquiry")
    public String inquiry(Model model) {
        return "contact/inquiry";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        return "contact/about";
    }
} 