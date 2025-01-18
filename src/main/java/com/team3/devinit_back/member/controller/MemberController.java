package com.team3.devinit_back.member.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.dto.MemberDto;
import com.team3.devinit_back.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketAddress;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController{
    private  final MemberService memberService;

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @RequestBody MemberDto memberDto){
        String socialId = userInfo.getName();
        String newNickname = memberDto.getNickName();

        memberService.updateNicknameBySocailId(socialId,memberDto.getNickName());

        return ResponseEntity.ok("닉네임 변경 성공");

    }
}
