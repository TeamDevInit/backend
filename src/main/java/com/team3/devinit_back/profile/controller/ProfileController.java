package com.team3.devinit_back.profile.controller;

import com.team3.devinit_back.profile.dto.ProfileDetailDto;
import com.team3.devinit_back.profile.dto.ProfileDto;
import com.team3.devinit_back.profile.service.ProfileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileDetailDto> getProfile(Authentication authentication) {
        String memberId = authentication.getName();

        ProfileDetailDto profileDetailDto = profileService.getProfile(memberId);
        return ResponseEntity.ok(profileDetailDto);
    }

    @GetMapping("/random")
    public ResponseEntity<List<ProfileDto>> getRandomProfiles() {
        List<ProfileDto> randomProfiles = profileService.getRandomProfiles();
        return ResponseEntity.ok(randomProfiles);
    }

    @PatchMapping
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDto profileDto, Authentication authentication) {
        try {
            String memberId = authentication.getName();

            profileService.updateProfile(memberId, profileDto);
            return ResponseEntity.ok("프로필이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("프로필 업데이트 실패: " + e.getMessage());
        }
    }
}
