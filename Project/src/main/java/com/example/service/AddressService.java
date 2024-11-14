package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Address;
import com.example.repository.AddressRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    
    private final AddressRepository addressRepository;
    
    // 주소 목록 조회
    public List<Address> getAddressList(Long memberId) {
        return addressRepository.findByMemberId(memberId);
    }
    
    // 주소 저장
    @Transactional
    public void saveAddress(Address address) {
        // 해당 회원의 주소가 하나도 없다면 첫 번째 주소를 기본 배송지로 설정
        List<Address> existingAddresses = addressRepository.findByMemberId(address.getMember().getId());
        if (existingAddresses.isEmpty()) {
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        addressRepository.save(address);
    }
    
    @Transactional
    public void deleteAddress(Long addressId, Long memberId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송지입니다."));
        
        // 로그인한 사용자의 배송지인지 확인
        if (!address.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        
        addressRepository.delete(address);
    }
} 