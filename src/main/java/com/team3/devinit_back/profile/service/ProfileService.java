package com.team3.devinit_back.profile.service;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.follow.dto.FollowCountResponse;
import com.team3.devinit_back.follow.service.FollowService;
import com.team3.devinit_back.global.amazonS3.service.S3Service;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.profile.dto.BoardSummaryResponse;
import com.team3.devinit_back.profile.dto.ProfileDetailResponse;
import com.team3.devinit_back.profile.dto.ProfileUpdateRequest;
import com.team3.devinit_back.profile.dto.ProfileResponse;
import com.team3.devinit_back.profile.entity.Profile;
import com.team3.devinit_back.profile.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final BoardRepository boardRepository;
    private final FollowService followService;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public ProfileDetailResponse getMyProfile(String memberId) {
        Profile profile = getProfileByMemberId(memberId);

        FollowCountResponse followCounts = followService.getFollowCounts(memberId);
        boolean isFollowing = false;

        return ProfileDetailResponse.fromEntity(profile, followCounts, isFollowing);
    }

    @Transactional(readOnly = true)
    public Page<BoardSummaryResponse> getBoardsByMemberId(String memberId, Pageable pageable) {
        Page<Board> boards = boardRepository.findByMemberId(memberId, pageable);

        if (boards.isEmpty()) {
            log.info("회원 ID {} 에 대한 게시물이 없습니다.", memberId);
        }

        return boards.map(BoardSummaryResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProfileDetailResponse getProfile(String profileId, String viewerId) {
        Profile profile = getProfileById(profileId);
        FollowCountResponse followCounts = followService.getFollowCounts(profile.getMember().getId());
        boolean isFollowing = viewerId != null && followService.isFollowing(viewerId, profile.getMember().getId());

        return ProfileDetailResponse.fromEntity(profile, followCounts, isFollowing);
    }

    @Transactional
    public String getProfileImage(String memberId) {
        return getProfileByMemberId(memberId).getMember().getProfileImage();
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> getRandomProfiles() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Profile> randomProfiles = profileRepository.findRandomProfiles(pageable);

        if (randomProfiles.isEmpty()) {
            log.warn("랜덤 조회 결과가 없습니다.");
        }

        return randomProfiles.stream()
            .map(profile -> new ProfileResponse(
                profile.getId(),
                profile.getMember().getNickName(),
                profile.getAbout(),
                profile.getMember().getProfileImage()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateProfile(String memberId, ProfileUpdateRequest request, MultipartFile newProfileImage) {
        Profile profile = isAuthorized(memberId);

        String newProfileImageUrl = handleProfileImage(profile.getMember(), newProfileImage);

        profile.getMember().updateProfile(request.getNickname(), newProfileImageUrl);
        profile.update(request.getAbout());
    }

    private String handleProfileImage(Member member, MultipartFile newProfileImage) {
        String oldProfileImageUrl = member.getProfileImage();

        if (newProfileImage != null && oldProfileImageUrl != null && !oldProfileImageUrl.isEmpty()
                && !s3Service.isDefaultProfileImage(oldProfileImageUrl)) {
            String oldFileName = extractFileNameFromUrl(oldProfileImageUrl);
            s3Service.deleteFile(oldFileName);
        }

        if (newProfileImage != null) {
            try {
                return s3Service.uploadFile(newProfileImage);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        return oldProfileImageUrl != null && !oldProfileImageUrl.isEmpty()
            ? oldProfileImageUrl : s3Service.getDefaultProfileImageUrl();
    }

    private Profile getProfileByMemberId(String memberId) {
        return profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
    }

    private Profile getProfileById(String profileId) {
        return profileRepository.findById(profileId)
            .orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
    }

    private Profile isAuthorized(String memberId) {
        return profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_DENIED));
    }

    private String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}