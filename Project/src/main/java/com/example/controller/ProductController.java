package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // 베스트 상품 페이지
    @GetMapping("/best-products")
    public String getBestProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "best-products"; // Thymeleaf 템플릿 이름
    }

    // 상품 추가 폼
    @GetMapping("/admin/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    // 상품 저장
    @PostMapping("/admin/save-product")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            product.setImageName(imageName);
            try {
                // 이미지 파일 저장 경로
                String uploadDir = "src/main/resources/static/product_IMG_/";
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }
                imageFile.transferTo(new File(uploadDir + imageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.saveProduct(product);
        return "redirect:/best-products";
    }
}