package com.example.controller.admin;

import com.example.model.Product;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    // 상품 목록 페이지
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/list-products";
    }

    // 상품 추가 폼 페이지
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product";
    }

    // 상품 추가 처리
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Model model) {
        if (!imageFile.isEmpty()) {
            try {
                // 원본 파일 이름 가져오기
                String originalFilename = imageFile.getOriginalFilename();

                // 파일 저장 경로
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
                model.addAttribute("errorMessage", "이미지 업로드에 실패했습니다.");
                return "admin/add-product";
            }
        }

        // 상품 저장
        productService.saveProduct(product);
        model.addAttribute("successMessage", "상품이 성공적으로 추가되었습니다.");
        return "redirect:/admin/products";
    }

    // 상품 수정 폼 페이지
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "admin/edit-product";
        } else {
            model.addAttribute("errorMessage", "해당 ID의 상품을 찾을 수 없습니다.");
            return "redirect:/admin/products";
        }
    }

    // 상품 수정 처리
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model) {
        Optional<Product> existingProductOpt = productService.getProductById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());

            if (!imageFile.isEmpty()) {
                try {
                    // 원본 파일 이름 가져오기
                    String originalFilename = imageFile.getOriginalFilename();

                    // 파일 저장 경로
                    String uploadDir = System.getProperty("user.dir") + "/uploads/product_IMG_/";
                    File uploadPath = new File(uploadDir);
                    if (!uploadPath.exists()) {
                        uploadPath.mkdirs();
                    }

                    // 기존 이미지 삭제 (선택 사항)
                    String existingImage = existingProduct.getImageName();
                    if (existingImage != null) {
                        File existingFile = new File(uploadDir + existingImage);
                        if (existingFile.exists()) {
                            existingFile.delete();
                        }
                    }

                    // 파일 저장
                    imageFile.transferTo(new File(uploadDir + originalFilename));

                    // Product 객체에 이미지 이름 설정
                    existingProduct.setImageName(originalFilename);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("errorMessage", "이미지 업로드에 실패했습니다.");
                    return "admin/edit-product";
                }
            }

            // 수정된 상품 저장
            productService.saveProduct(existingProduct);
            model.addAttribute("successMessage", "상품이 성공적으로 수정되었습니다.");
            return "redirect:/admin/products";
        } else {
            model.addAttribute("errorMessage", "해당 ID의 상품을 찾을 수 없습니다.");
            return "redirect:/admin/products";
        }
    }

    // 상품 삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            // 이미지 파일 삭제 (선택 사항)
            String imageName = productOpt.get().getImageName();
            if (imageName != null) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/product_IMG_/";
                File imageFile = new File(uploadDir + imageName);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }

            // 상품 삭제
            productService.deleteProduct(id);
            model.addAttribute("successMessage", "상품이 성공적으로 삭제되었습니다.");
        } else {
            model.addAttribute("errorMessage", "해당 ID의 상품을 찾을 수 없습니다.");
        }

        return "redirect:/admin/products";
    }
}