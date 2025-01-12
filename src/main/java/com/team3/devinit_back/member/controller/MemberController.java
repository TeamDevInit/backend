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
public class MemberController {
    private  final MemberService memberService;

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @RequestBody MemberDto memberDto){
        String socialId = userInfo.getName();
        String newNickname = memberDto.getNickName();
        if(memberService.isNicknameExists(newNickname)){
            return new ResponseEntity<>("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT);
        }
        else{
            boolean isUpdated = memberService.updateNicknameBySocailId(socialId,memberDto.getNickName());
            if (isUpdated) {
                return new ResponseEntity<>("닉네임 변경 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("닉네임 변경 실패", HttpStatus.NOT_FOUND);
            }
        }

    }
}
