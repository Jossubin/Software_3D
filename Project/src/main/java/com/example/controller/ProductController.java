// src/main/java/com/example/controller/ProductController.java
package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 베스트 상품 페이지
    @GetMapping("/test_newhome")
    public String getBestProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "test_newhome"; // Thymeleaf 템플릿 이름
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
        return "redirect:/test_newhome"; // 리다이렉트할 페이지 수정 가능
    }

    // 상품 상세 페이지
    @GetMapping("/product-detail/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "product-detail"; // Thymeleaf 템플릿 이름
        } else {

            return "redirect:/error";
        }
    }

    // 상품 상세 페이지 처리 메서드 추가
    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "product-detail";
        } else {
            return "redirect:/test_newhome";
        }
    }
}