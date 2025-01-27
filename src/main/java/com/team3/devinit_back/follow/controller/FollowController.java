package com.team3.devinit_back.follow.controller;

import com.team3.devinit_back.follow.service.FollowService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;

    @Operation(
        summary = "팔로우 요청",
        description = "특정 사용자를 팔로우합니다."
    )
    @PostMapping("/{receiverId}")
    public ResponseEntity<String> follow(@PathVariable("receiverId") String receiverId,
                                         @AuthenticationPrincipal CustomOAuth2User userInfo) {
        Member sender = getMemberFromUserInfo(userInfo);
        followService.follow(sender.getId(), receiverId);
        return ResponseEntity.ok("팔로우 성공");
    }

    @Operation(
        summary = "언팔로우 요청",
        description = "특정 사용자에 대한 팔로우를 취소합니다."
    )
    @DeleteMapping("/{receiverId}")
    public ResponseEntity<String> unfollow(@PathVariable("receiverId") String receiverId,
                                           @AuthenticationPrincipal CustomOAuth2User userInfo) {
        Member sender = getMemberFromUserInfo(userInfo);
        followService.unfollow(sender.getId(), receiverId);
        return ResponseEntity.ok("언팔로우 성공");
    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}