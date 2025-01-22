package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.SkillRequestDto;
import com.team3.devinit_back.resume.dto.SkillResponseDto;
import com.team3.devinit_back.resume.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<List<SkillResponseDto>> addSkill(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                         @RequestBody SkillRequestDto skillRequestDto) {
        Member member = getMemberFromUserInfo(userInfo);
        List<SkillResponseDto> skillResponseDto = skillService.addSkill(member, skillRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(skillResponseDto);
    }

    @PatchMapping
    public ResponseEntity<List<SkillResponseDto>> updateSkill(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                        @RequestBody SkillRequestDto skillRequestDto) {
        Member member = getMemberFromUserInfo(userInfo);
        List<SkillResponseDto> updateSkills = skillService.updateSkill(member, skillRequestDto);
        return ResponseEntity.ok(updateSkills);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                            @PathVariable Long skillId) {
        Member member = getMemberFromUserInfo(userInfo);
        skillService.deleteSkill(member, skillId);
        return ResponseEntity.noContent().build();
    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}