package com.team3.devinit_back.member.service;


import com.team3.devinit_back.global.amazonS3.service.S3Service;
import com.team3.devinit_back.member.dto.*;
import com.team3.devinit_back.member.entity.Member;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("github")) {

            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String socialId = oAuth2Response.getProviderId();
        String socialProvider = oAuth2Response.getProvider();
        Member existData = memberRepository.findBySocialId(socialId);

        if(existData == null){
            Member member = new Member();
            member.setSocialId(socialId);
            member.setSocialProvider(socialProvider);
            member.setRole("ROLE_USER");
            member.setProfileImage(s3Service.getDefaultProfileImageUrl());
            String nickname;
            do{
                nickname = randomNickname.generate();
            } while (memberRepository.existsByNickName(nickname));
            member.setNickName(nickname);

            memberRepository.save(member);


            Profile profile = Profile.builder()
                    .member(member)
                    .about("")
                    .build();
            profileRepository.save(profile);

            Resume resume = new Resume();
            resume.setMember(member);
            resumeRepository.save(resume);

            MemberDto memberDto = new MemberDto();
            memberDto.setName(socialId);
            memberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(memberDto);
        }
        else{

            memberRepository.save(existData);

            MemberDto memberDto = new MemberDto();
            memberDto.setName(existData.getSocialId());
            memberDto.setRole(existData.getRole());

            return new CustomOAuth2User(memberDto);
        }

    }
}
