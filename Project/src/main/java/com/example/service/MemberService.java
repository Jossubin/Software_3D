package com.example.service;

import com.example.model.Member;
import com.example.repository.AddressRepository;
import com.example.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, AddressRepository addressRepository) {
        this.memberRepository = memberRepository;
        this.addressRepository = addressRepository;
    }

    public void register(Member member) {
        // 이메일 중복 체크 (필요한 경우)
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        memberRepository.save(member);
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    @Transactional
    public void updateMember(Member member) {
        try {
            Member existingMember = memberRepository.findById(member.getId())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

            // 기존 회원 정보 업데이트
            existingMember.setName(member.getName());
            if (member.getPassword() != null && !member.getPassword().isEmpty()) {
                existingMember.setPassword(member.getPassword());
            }
            existingMember.setPhone(member.getPhone());
            
            // 프로필 이미지 경로 업데이트
            if (member.getProfileImagePath() != null && !member.getProfileImagePath().isEmpty()) {
                existingMember.setProfileImagePath(member.getProfileImagePath());
                System.out.println("프로필 이미지 경로 업데이트: " + member.getProfileImagePath()); // 디버깅용 로그
            }

            memberRepository.save(existingMember);
            System.out.println("회원 정보 업데이트 완료"); // 디버깅용 로그
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("회원 정보 업데이트 실패: " + e.getMessage()); // 디버깅용 로그
            throw new RuntimeException("회원 정보 업데이트 중 오류 발생: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteMember(Long memberId) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

            // 1. 먼저 회원의 주소 정보를 삭제
            addressRepository.deleteByMemberId(memberId);

            // 2. 그 다음 회원 삭제
            memberRepository.delete(member);

            System.out.println("회원 삭제 완료: " + member.getEmail());

        } catch (Exception e) {
            System.out.println("회원 삭제 중 오류 발생: " + e.getMessage());
            throw new IllegalStateException("회원 삭제 중 오류가 발생했습니다.", e);
        }

    }

    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String saveProfileImage(MultipartFile file, Long memberId) throws IOException {
        try {
            String fileName = memberId + ".jpg";
            String uploadDir = "C:/Users/cjdeh/Documents/GitHub/Software_3D/Project/src/main/resources/static/memberimg/";
            String filePath = uploadDir + fileName;

            System.out.println("저장 경로: " + filePath);

            // 디렉토리 생성
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                System.out.println("디렉토리 생성 결과: " + created);
            }

            // 기존 파일 삭제
            File dest = new File(filePath);
            if (dest.exists()) {
                dest.delete();
                // 파일 시스템 동기화를 위한 짧은 대기
                Thread.sleep(100);
            }
            
            // 새 파일 저장
            Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("파일 저장 완료: " + filePath);

            // 파일 시스템 동기화를 위한 짧은 대기
            Thread.sleep(100);

            return "/memberimg/" + fileName;
        } catch (Exception e) {
            System.out.println("파일 저장 중 오류: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("이미지 저장 실패: " + e.getMessage());
        }
    }

    public void updateMemberProfileImage(Long memberId, MultipartFile profileImage) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        if (!profileImage.isEmpty()) {
            String imagePath = saveProfileImage(profileImage, memberId);
            member.setProfileImagePath(imagePath);
            memberRepository.save(member);
        }
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}