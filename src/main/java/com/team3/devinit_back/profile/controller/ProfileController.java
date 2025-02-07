package com.team3.devinit_back.profile.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.profile.dto.*;
import com.team3.devinit_back.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final MemberService memberService;

    @Operation(
        summary = "내 프로필 상세 조회",
        description = "로그인한 사용자의 프로필 정보를 상세히 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<ProfileDetailResponse> getMyProfile(@AuthenticationPrincipal CustomOAuth2User userInfo) {
        Member member = getMemberFromUserInfo(userInfo);
        ProfileDetailResponse response = profileService.getMyProfile(member.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "프로필 이미지 조회", description = "로그인한 사용자의 프로필 이미지를 반환합니다.")
    @GetMapping("/profile/image")
    public ResponseEntity<ProfileImageResponseDto> getProfileImage(@AuthenticationPrincipal CustomOAuth2User userInfo) {
        String memberId = getMemberFromUserInfo(userInfo).getId();
        String profileImageUrl = profileService.getProfileImage(memberId);
        return ResponseEntity.ok(new ProfileImageResponseDto(profileImageUrl));
    }


    @Operation(
        summary = "상대 프로필 상세 조회",
        description = "특정 사용자의 프로필 정보를 상세히 조회합니다. "
                    + "로그인한 사용자는 상대 사용자를 팔로우 중인지 여부를 확인할 수 있습니다."
    )
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileDetailResponse> getProfile(
        @PathVariable("profileId") String profileId,
        @AuthenticationPrincipal CustomOAuth2User userInfo) {
        String viewerId = (userInfo != null) ? getMemberFromUserInfo(userInfo).getId() : null;
        ProfileDetailResponse response = profileService.getProfile(profileId, viewerId);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "사용자의 작성 게시물 리스트 조회",
        description = "특정 사용자가 작성한 게시물의 목록을 페이지네이션으로 조회합니다. "
                    + "정렬 기준은 기본적으로 작성일(desc)입니다."
    )
    @GetMapping("/boards/{memberId}")
    public ResponseEntity<Page<BoardSummaryResponse>> getBoardsByMemberId(
        @PathVariable("memberId") String memberId,
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardSummaryResponse> response = profileService.getBoardsByMemberId(memberId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "프로필 랜덤 조회",
        description = "회원들 중 랜덤하게 10개의 프로필 정보를 조회합니다."
    )
    @GetMapping("/random")
    public ResponseEntity<List<ProfileResponse>> getRandomProfiles() {
        List<ProfileResponse> randomProfiles = profileService.getRandomProfiles();
        return ResponseEntity.ok(randomProfiles);
    }

    @Operation(
        summary = "프로필 정보 수정",
        description = "로그인한 사용자가 자신의 프로필 정보를 수정합니다. "
                    + "닉네임, 한 줄 소개, 프로필 이미지를 업데이트할 수 있습니다.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProfileDetailResponse> updateProfile
        (@AuthenticationPrincipal CustomOAuth2User userInfo,
         @RequestPart(value = "profile") ProfileUpdateRequest request,
         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        String memberId = getMemberFromUserInfo(userInfo).getId();
        profileService.updateProfile(memberId, request, profileImage);
        ProfileDetailResponse updateProfile = profileService.getMyProfile(memberId);
        return ResponseEntity.ok(updateProfile);
    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}