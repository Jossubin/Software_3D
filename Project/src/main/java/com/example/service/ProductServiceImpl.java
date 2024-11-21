package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        // 사용자의 요청에 따라 초기 페이지 정렬 방식은 createdDate기준으로 정렬 !
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    @Override
    public List<Product> getAllProductsSorted(String sortBy) {
        switch (sortBy) {
            case "latest":
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
            case "popular":
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "popularity"));
            case "sales":
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "salesVolume"));
            default:
                // 기본 정렬 (최신순)
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        // 저장 시 createdDate가 null이면 현재 시간으로 설정
        if (product.getCreatedDate() == null) {
            product.setCreatedDate(java.time.LocalDateTime.now());
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }
}