package com.team3.devinit_back.follow.controller;

import com.team3.devinit_back.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{receiverId}")
    public ResponseEntity<String> follow(@PathVariable String receiverId, Authentication authentication) {
        String senderId = authentication.getName();
        followService.follow(senderId, receiverId);
        return ResponseEntity.ok("팔로우 성공");
    }

    @DeleteMapping("/{receiverId}")
    public ResponseEntity<String> unfollow(@PathVariable String receiverId, Authentication authentication) {
        String senderId = authentication.getName();
        followService.unfollow(senderId, receiverId);
        return ResponseEntity.ok("언팔로우 성공");
    }
}