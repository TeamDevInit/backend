package com.team3.devinit_back.member.service;

import com.team3.devinit_back.dto.*;
import com.team3.devinit_back.member.dto.*;
import com.team3.devinit_back.member.entity.MemberEntity;
import com.team3.devinit_back.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository){
        this.memberRepository =memberRepository;
    }
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
        else if (registrationId.equals("google")) {  //---> 카카오로 변경 카카오 데이터 형식이 어떤지를 모름

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //유저 정보확인 및 기존 회원 여부 판별
        String socialId = oAuth2Response.getProviderId();
        String socialProvider = oAuth2Response.getProvider();
        MemberEntity existData =memberRepository.findBySocialId(socialId);

        if(existData == null){ // 없으면 생성
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setSocialId(socialId);
            memberEntity.setSocialProvider(socialProvider);
            memberEntity.setName(oAuth2Response.getName());
            memberEntity.setRole("ROLE_USER");

            memberRepository.save(memberEntity);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(socialId);
            memberDto.setName(oAuth2Response.getName());
            memberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(memberDto);
        }
        else{ // 있으면 변경가능한 값만 재세팅 하고 저장
            existData.setName(oAuth2Response.getName());

            memberRepository.save(existData);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(existData.getSocialId());
            memberDto.setName(oAuth2Response.getName());
            memberDto.setRole(existData.getRole());

            return new CustomOAuth2User(memberDto);
        }

    }
}
