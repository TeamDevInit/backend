package com.team3.devinit_back.member.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.dto.DailyBoardCountDto;
import com.team3.devinit_back.member.dto.MemberDto;
import com.team3.devinit_back.member.repository.DailyBoardCountRepository;
import com.team3.devinit_back.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.SocketAddress;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController{
    private final MemberService memberService;

    @PatchMapping("/nickname")
    @Operation(
            summary = "닉네임 변경",
            description = "로그인한 사용자의 프로필 이미지를 변경합니다. + 중복 불가")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @RequestBody MemberDto memberDto){
        String socialId = userInfo.getName();
        String newNickname = memberDto.getNickName();

        memberService.updateNicknameBySocailId(socialId,memberDto.getNickName());

        return ResponseEntity.ok("닉네임 변경 성공");

    }

    @GetMapping("/stats/{memberId}/{year}")
    public ResponseEntity<List<DailyBoardCountDto>> getYearlyStats(@PathVariable("memberId") String id,
                                                                   @PathVariable("year") int year) {
        List<DailyBoardCountDto> stats = memberService.getYearlyStats(id, year);
        return ResponseEntity.ok(stats);
    }
}
