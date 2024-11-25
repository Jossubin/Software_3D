package com.example.service;

import com.example.model.Faq;
import java.util.List;

public interface FaqService {
    List<Faq> getAllFaqs();
    Faq saveFaq(Faq faq);
    void deleteFaq(Long id);
    Faq getFaqById(Long id);
} 