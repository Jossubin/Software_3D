package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/address")
    public String addressList(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        List<Address> addresses = addressService.getAddressList(loginMember.getId());
        model.addAttribute("addresses", addresses);
        model.addAttribute("newAddress", new Address());
        return "list";
    }

    @PostMapping("/address/add")
    public String addAddress(@ModelAttribute Address address, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        address.setMember(loginMember);
        addressService.saveAddress(address);
        return "redirect:/address";
    }

    @DeleteMapping("/address/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteAddress(@PathVariable Long id, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            addressService.deleteAddress(id, loginMember.getId());
            return ResponseEntity.ok("삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("삭제 실패: " + e.getMessage());
        }
    }
} 