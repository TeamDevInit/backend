package com.team3.devinit_back.profile.service;

import com.team3.devinit_back.follow.repository.FollowRepository;
import com.team3.devinit_back.follow.service.FollowService;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.repository.MemberRepository;
import com.team3.devinit_back.profile.dto.ProfileDetailDto;
import com.team3.devinit_back.profile.dto.ProfileDto;
import com.team3.devinit_back.profile.entity.Profile;
import com.team3.devinit_back.profile.repository.ProfileRepository;
import com.team3.devinit_back.resume.dto.ResumeDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final FollowService followService;
    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public ProfileDetailDto getMyProfile(String memberId) {
        Profile profile = profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("프로필 정보를 찾을 수 없습니다."));

        MemberEntity member = profile.getMember();
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

        MemberEntity member = profile.getMember();
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
    public void updateProfile(String memberId, ProfileDto profileDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        member.setNickName(profileDto.getNickname());
        member.setProfileImage(profileDto.getProfileImage());
        Profile profile = member.getProfile();
        if (profile != null) {
            profile.setAbout(profileDto.getAbout());
        }
    }
}