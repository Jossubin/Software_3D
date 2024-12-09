package com.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.Member;
import com.example.model.Order;
import com.example.model.Product;
import com.example.service.MemberService;
import com.example.service.OrderService;
import com.example.service.ProductService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService; // OrderService 주입

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Member member, Model model, RedirectAttributes redirectAttributes) {
        // 이메일 중복 체크
        if (memberService.isEmailExists(member.getEmail())) {
            model.addAttribute("emailError", true);
            return "register";
        }

        try {
            memberService.register(member);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";  // login.html 템플릿을 반환
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Member member, HttpSession session) {
        try {
            Member authenticatedMember = memberService.login(member.getEmail(), member.getPassword());
            // admin@admin 계정 체크
            if ("admin@admin".equals(authenticatedMember.getEmail())) {
                authenticatedMember.setAdmin(true);
            }

            // 세션에 저장
            session.setAttribute("loginMember", authenticatedMember);
            System.out.println("세션 저장됨: " + session.getId());  // 디버깅용

            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/home";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        
        if (loginMember == null) {
            return "redirect:/login";
        }
        
        // 매번 최신 데이터를 DB에서 조회
        Member updatedMember = memberService.findById(loginMember.getId());
        model.addAttribute("member", updatedMember);
        
        // 세션도 최신 데이터로 업데이트
        session.setAttribute("loginMember", updatedMember);
        
        return "mypage";
    }

    @GetMapping("/member/update")
    public String editProfileForm(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("member", loginMember);
        return "edit-profile";
    }

    @PostMapping("/member/update")
    public String editProfile(@ModelAttribute Member member,
                             @RequestParam("profileImage") MultipartFile profileImage,
                             RedirectAttributes redirectAttributes, 
                             HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            member.setId(loginMember.getId());

            if (member.getPassword() == null || member.getPassword().isEmpty()) {
                member.setPassword(loginMember.getPassword());
            }

            // 프로필 이미지 처리
            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    String imagePath = memberService.saveProfileImage(profileImage, member.getId());
                    member.setProfileImagePath(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("error", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
                    return "redirect:/member/update";
                }
            } else {
                member.setProfileImagePath(loginMember.getProfileImagePath());
            }

            // 회원 정보 업데이트
            memberService.updateMember(member);
            
            // 업데이트 후 즉시 최신 데이터 조회하여 세션 갱신
            Member updatedMember = memberService.findById(member.getId());
            session.setAttribute("loginMember", updatedMember);
            
            redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었습니다.");
            return "redirect:/mypage?t=" + System.currentTimeMillis(); // URL에 타임스탬프 추가
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "회원정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/member/update";
        }
    }

    @PostMapping("/member/delete")
    public String deleteMember(HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            System.out.println("회원 탈퇴 요청: " + loginMember.getEmail());
            memberService.deleteMember(loginMember.getId());

            // 세션 무효화
            session.invalidate();

            redirectAttributes.addFlashAttribute("message", "회원탈퇴가 완료되었습니다.");
            return "redirect:/";

        } catch (Exception e) {
            System.out.println("회원 탈퇴 실패: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "회원탈퇴 처리 중 오류가 발생했습니다.");
            return "redirect:/member/update";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션 무효화
        session.invalidate();

        redirectAttributes.addFlashAttribute("message", "로그아웃 되었습니다.");
        return "redirect:/";
    }

    @GetMapping("/index")
    public String homepage(Model model) {
        // 상품 정보 추가
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "home";
    }

    ////////
    @GetMapping("/member-home")
    public String home11(Model model, HttpSession session) {
        // 로그인된 회원 정보를 가져옴
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember != null) {
            model.addAttribute("member", loginMember);
        }

        // 상품 정보 추가
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "home";
    }

    @GetMapping("/productImage")
    public String showProductImage() {
        return "productImage";  // productImage.html 템플릿을 반환
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션에서 로그인된 회원 정보 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");

        // 로그인 체크
        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            // 주문 처리
            orderService.checkout(loginMember);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("message", "주문이 성공적으로 완료되었습니다.");

            return "redirect:/mypage";
        } catch (RuntimeException e) {
            // 에러 메시지 설정
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/memberimg/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("C:/Users/cjdeh/Documents/GitHub/Software_3D/Project/src/main/resources/static/memberimg/" + fileName);
            Resource resource = new FileSystemResource(filePath.toFile());
            
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}