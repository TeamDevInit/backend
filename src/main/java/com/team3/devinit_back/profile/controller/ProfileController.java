package com.team3.devinit_back.profile.controller;

import com.team3.devinit_back.profile.dto.ProfileDetailDto;
import com.team3.devinit_back.profile.dto.ProfileDto;
import com.team3.devinit_back.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "내 프로필 상세 조회")
    @GetMapping("/me")
    public ResponseEntity<ProfileDetailDto> getMyProfile(Authentication authentication) {
        String memberId = authentication.getName();
        ProfileDetailDto profileDetailDto = profileService.getMyProfile(memberId);
        return ResponseEntity.ok(profileDetailDto);
    }

    @Operation(summary = "상대 프로필 상세 조회")
    @GetMapping("{profileId}")
    public ResponseEntity<ProfileDetailDto> getProfile(@PathVariable String profileId) {
        ProfileDetailDto profileDetailDto = profileService.getProfileById(profileId);
        return ResponseEntity.ok(profileDetailDto);
    }

    @Operation(summary = "프로필 랜덤 조회")
    @GetMapping("/random")
    public ResponseEntity<List<ProfileDto>> getRandomProfiles() {
        List<ProfileDto> randomProfiles = profileService.getRandomProfiles();
        return ResponseEntity.ok(randomProfiles);
    }

    @Operation(
        summary = "프로필 정보 수정",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> updateProfile
        (@RequestPart(value = "profile", required = true) ProfileDetailDto profileDetailDto,
         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
         Authentication authentication) {
        try {
            String memberId = authentication.getName();

            profileService.updateProfile(memberId, profileDetailDto, profileImage);
            return ResponseEntity.ok("프로필이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("프로필 업데이트 실패: " + e.getMessage());
        }
    }
}