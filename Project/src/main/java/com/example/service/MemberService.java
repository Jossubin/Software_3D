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

    public void updateMember(Member member) {
        // 기존 회원 검증
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        // 이메일 중복 검사 (다른 회원과 중복되는지)
        Optional<Member> emailCheck = memberRepository.findByEmail(member.getEmail());
        if (emailCheck.isPresent() && !emailCheck.get().getId().equals(member.getId())) {
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }

        // 회원 정보 업데이트
        memberRepository.save(member);
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        // 기존 이미지 경로 가져오기
        String existingImagePath = member.getProfileImagePath();

        // 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 새 이미지 저장 경로 설정 (회원 ID로 파일 이름 설정)
        String fileName = memberId + fileExtension;
        Path directoryPath = Paths.get("Project/src/main/resources/static/memberimg");
        
        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        
        Path path = directoryPath.resolve(fileName);

        // 기존 파일 삭제
        if (existingImagePath != null) {
            Files.deleteIfExists(Paths.get(existingImagePath));
        }

        // 새 파일 저장
        Files.write(path, file.getBytes());

        // 새로운 이미지 경로 반환
        return "/memberimg/" + fileName; // 웹 경로로 반환
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
}