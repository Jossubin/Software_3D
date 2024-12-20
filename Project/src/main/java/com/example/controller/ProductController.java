package com.example.controller;

import com.example.model.Member;
import com.example.model.Product;
import com.example.service.ProductService;
import com.example.service.CartService;
import com.example.dto.CartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.repository.ProductRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    // 메인 페이지 - 새로운 상품만 조회
    @GetMapping("/home")
    public String getHomePage(Model model) {
        int newProductLimit = 6; // 원하는 개수 설정
        List<Product> newProducts = productService.getNewProducts(newProductLimit);
        model.addAttribute("newProducts", newProducts);
        return "home"; // Thymeleaf 템플릿 이름
    }

    // 상품 추가 폼
    @GetMapping("/admin/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product";
    }

    // 상품 저장
    @PostMapping("/admin/save-product")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                // 원본 파일 이름 가져오기
                String originalFilename = imageFile.getOriginalFilename();

                // 이미지 파일 저장 경로
                String uploadDir = System.getProperty("user.dir") + "/uploads/product_IMG_/";
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                // 파일 저장
                imageFile.transferTo(new File(uploadDir + originalFilename));

                // Product 객체에 이미지 이름 설정
                product.setImageName(originalFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.saveProduct(product);
        return "redirect:/home"; // 리다이렉트할 페이지 수정 가능
    }

    // 상품 상세 페이지
    @GetMapping("/product-detail/{id}")
    public String getProductDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "product-detail";
        } else {
            return "redirect:/home";  // 상품이 없을 경우 메인 페이지로 리다이렉트
        }
    }

    // 장바구니에 상품 추가
    @PostMapping("/add-to-cart")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request, HttpSession session) {
        // 세션 체크 추가
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            cartService.addToCart(
                    loginMember,
                    request.getProductId(),
                    request.getQuantity(),
                    request.getSize(),
                    request.getColor()
            );
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "장바구니에 추가되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Shop 페이지
    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String category, Model model) {
        List<Product> products;

        if (category != null && !category.isEmpty()) {
            // 특정 카테고리의 상품만 가져오기
            products = productService.getProductsByCategory(category);
        } else {
            // 카테고리가 선택되지 않았을 경우 모든 상품 가져오기
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("category", category);
        return "shop";
    }
    @GetMapping("/shop/search")
public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
    List<Product> searchResults = productService.searchProducts(keyword);
    model.addAttribute("products", searchResults);
    model.addAttribute("searchKeyword", keyword);
        return "shop";
    }
}