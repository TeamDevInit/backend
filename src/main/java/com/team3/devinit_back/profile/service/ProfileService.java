package com.team3.devinit_back.profile.service;

import com.team3.devinit_back.amazonS3.service.S3Service;
import com.team3.devinit_back.follow.service.FollowService;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.repository.MemberRepository;
import com.team3.devinit_back.profile.dto.ProfileDetailDto;
import com.team3.devinit_back.profile.dto.ProfileDto;
import com.team3.devinit_back.profile.entity.Profile;
import com.team3.devinit_back.profile.repository.ProfileRepository;
import com.team3.devinit_back.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final FollowService followService;
    private final S3Service s3Service;
    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public ProfileDetailDto getMyProfile(String memberId) {
        Profile profile = profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("프로필 정보를 찾을 수 없습니다."));

        Member member = profile.getMember();
        ProfileDetailDto dto = new ProfileDetailDto();

        dto.setNickname(member.getNickName());
        dto.setProfileImage(member.getProfileImage());
        dto.setAbout(profile.getAbout());
        dto.setBoardCount(profile.getBoardCount());
        dto.setFollowerCount(followService.getFollowerCount(memberId));
        dto.setFollowingCount(followService.getFollowingCount(memberId));

        return dto;
    }

    @Transactional(readOnly = true)
    public ProfileDetailDto getProfileById(String profileId) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new RuntimeException("프로필 정보를 찾을 수 없습니다."));

        Member member = profile.getMember();
        ProfileDetailDto dto = new ProfileDetailDto();

        dto.setNickname(member.getNickName());
        dto.setProfileImage(member.getProfileImage());
        dto.setAbout(profile.getAbout());
        dto.setBoardCount(profile.getBoardCount());
        dto.setFollowerCount(followService.getFollowerCount(member.getId()));
        dto.setFollowingCount(followService.getFollowingCount(member.getId()));

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ProfileDto> getRandomProfiles() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Profile> randomProfiles = profileRepository.findRandomProfiles(pageable);

        return randomProfiles.stream()
            .map(profile -> {
                ProfileDto dto = new ProfileDto();
                dto.setNickname(profile.getMember().getNickName());
                dto.setProfileImage(profile.getMember().getProfileImage());
                dto.setAbout(profile.getAbout());
                return dto;
            }).collect(Collectors.toList());
    }

    @Transactional
    public void updateProfile(String memberId, ProfileDetailDto profileDetailDto, MultipartFile newProfileImage) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 기존 프로필 이미지 삭제
        String oldProfileImageUrl = member.getProfileImage();
        if (profileDetailDto.getProfileImage() != null && oldProfileImageUrl != null && !oldProfileImageUrl.isEmpty()) {
            String oldFileName = extractFileNameFromUrl(oldProfileImageUrl);
            s3Service.deleteFile(oldFileName);
        }

        // 새 프로필 이미지 업로드
        String newProfileImageUrl = null;
        if (newProfileImage != null) {
            try {
                newProfileImageUrl = s3Service.uploadFile(newProfileImage);
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 업로드 실패: " + e.getMessage(), e);
            }
        } else if (newProfileImage == null && (oldProfileImageUrl == null || oldProfileImageUrl.isEmpty())) {
            // 기본 이미지 설정
            newProfileImageUrl = s3Service.getDefaultProfileImageUrl();
        }

        member.setNickName(profileDetailDto.getNickname());
        member.setProfileImage(newProfileImageUrl != null ? newProfileImageUrl : member.getProfileImage());

        Profile profile = member.getProfile();
        if (profile != null) {
            profile.setAbout(profileDetailDto.getAbout());
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}