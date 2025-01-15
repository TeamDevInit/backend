package com.team3.devinit_back.member.service;


import com.team3.devinit_back.member.dto.*;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.repository.MemberRepository;
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
    private final RandomNickname randomNickname;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        //어디서온 요청인지 확인후 양식에 맞게 변수에 저장
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

        //유저 정보확인 및 기존 회원 여부 판별
        String socialId = oAuth2Response.getProviderId();
        String socialProvider = oAuth2Response.getProvider();
        Member existData =memberRepository.findBySocialId(socialId);

        //---프로필 생성 추가
        if(existData == null){ // 없으면 생성
            Member member = new Member();
            member.setSocialId(socialId);
            member.setSocialProvider(socialProvider);
            //memberEntity.setName(oAuth2Response.getName());
            member.setRole("ROLE_USER");
            member.setProfileImage("https://elice-devinit.s3.ap-northeast-2.amazonaws.com/default_profile_image.png");
            String nickname;
            do{
                nickname = randomNickname.generate();
            } while (memberRepository.existsByNickName(nickname));
            member.setNickName(nickname);

            memberRepository.save(member);
            MemberDto memberDto = new MemberDto();
            memberDto.setName(socialId);
            //memberDto.setName(oAuth2Response.getName());
            memberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(memberDto);
        }
        else{ // 있으면 변경가능한 값만 재세팅 하고 저장
            //existData.setName(oAuth2Response.getName());

            memberRepository.save(existData);

            MemberDto memberDto = new MemberDto();
            memberDto.setName(existData.getSocialId());
            //memberDto.setName(oAuth2Response.getName());
            memberDto.setRole(existData.getRole());

            return new CustomOAuth2User(memberDto);
        }

    }
}
