package com.team3.devinit_back.member.service;


import com.team3.devinit_back.global.amazonS3.service.S3Service;
import com.team3.devinit_back.member.dto.*;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.oauth2.OAuth2ResponseFactory;
import com.team3.devinit_back.member.repository.MemberRepository;
import com.team3.devinit_back.profile.entity.Profile;
import com.team3.devinit_back.profile.repository.ProfileRepository;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final RandomNickname randomNickname;
    private final ResumeRepository resumeRepository;
    private final S3Service s3Service;
    private final OAuth2ResponseFactory oAuth2ResponseFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = oAuth2ResponseFactory.getOAuth2Response(registrationId, oAuth2User.getAttributes());

        String socialId = oAuth2Response.getProviderId();
        String socialProvider = oAuth2Response.getProvider();
        return getOrCreateMember(socialId, socialProvider);
    }
    private OAuth2User getOrCreateMember(String socialId, String socialProvider) {
        return memberRepository.findBySocialId(socialId)
                .map(this::toCustomOAuth2User)
                .orElseGet(() -> createMember(socialId, socialProvider));
    }

    private OAuth2User createMember(String socialId, String socialProvider) {
        Member member = Member.builder()
                .socialId(socialId)
                .socialProvider(socialProvider)
                .role("ROLE_USER")
                .profileImage(s3Service.getDefaultProfileImageUrl())
                .nickName(makeUniqueNickname())
                .build();

        memberRepository.save(member);
        createProfileAndResume(member);
        return toCustomOAuth2User(member);
    }

    private void createProfileAndResume(Member member) {
        Profile profile = Profile.builder()
                .member(member)
                .about("")
                .build();
        profileRepository.save(profile);

        Resume resume = new Resume();
        resume.setMember(member);
        resumeRepository.save(resume);
    }

    private String makeUniqueNickname() {
        String nickname;
        do {
            nickname = randomNickname.generate();
        } while (memberRepository.existsByNickName(nickname));
        return nickname;
    }

    private OAuth2User toCustomOAuth2User(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setName(member.getSocialId());
        memberDto.setRole(member.getRole());
        return new CustomOAuth2User(memberDto);
    }
}
