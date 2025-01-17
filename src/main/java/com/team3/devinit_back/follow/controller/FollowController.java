package com.team3.devinit_back.follow.controller;

import com.team3.devinit_back.follow.service.FollowService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;

    @PostMapping("/{receiverId}")
    public ResponseEntity<String> follow(@PathVariable("receiverId") String receiverId,
                                         @AuthenticationPrincipal CustomOAuth2User userInfo) {
        Member sender = getMemberFromUserInfo(userInfo);
        followService.follow(sender.getId(), receiverId);
        return ResponseEntity.ok("팔로우 성공");
    }

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