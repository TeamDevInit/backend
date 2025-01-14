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
    public ProfileDetailDto getProfile(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        Profile profile = member.getProfile();

        ProfileDetailDto profileDetailDto = new ProfileDetailDto();
        profileDetailDto.setNickname(member.getNickName());
        profileDetailDto.setProfileImage(member.getProfileImage());
        profileDetailDto.setAbout(profile.getAbout());
        profileDetailDto.setBoardCount(profile.getBoardCount());
        profileDetailDto.setFollowerCount(followService.getFollowerCount(memberId));
        profileDetailDto.setFollowingCount(followService.getFollowingCount(memberId));

        return profileDetailDto;
    }

    @Transactional(readOnly = true)
    public List<ProfileDto> getRandomProfiles() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Profile> randomProfiles = profileRepository.findRandomProfiles(pageable);

        return randomProfiles.stream()
            .map(profile -> {
                ProfileDto profileDto = new ProfileDto();
                profileDto.setNickname(profile.getMember().getNickName());
                profileDto.setProfileImage(profile.getMember().getProfileImage());
                profileDto.setAbout(profile.getAbout());
                return profileDto;
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
