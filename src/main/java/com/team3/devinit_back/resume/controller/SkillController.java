package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.SkillRequestDto;
import com.team3.devinit_back.resume.dto.SkillResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ResumeService;
import com.team3.devinit_back.resume.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;
    private final MemberService memberService;
    private final ResumeService resumeService;


    @Operation(
            summary = "스킬 수정",
            description = "로그인한 사용자가 자신의 스킬 정보를 생성합니다. "
                    + "요청 본문에는 스킬이름이 포함되어야 합니다."
    )
    @PostMapping
    public ResponseEntity<List<SkillResponseDto>> createSkill(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @RequestBody SkillRequestDto skillRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<SkillResponseDto> skillResponseDtos = skillService.createSkill(resume, skillRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(skillResponseDtos);
    }
    @Operation(
            summary = "스킬 수정",
            description = "로그인한 사용자가 자신의 스킬 정보를 수정합니다. "
                    + "요청 본문에는 스킬이름이 포함되어야 합니다."
    )
    @PatchMapping
    public ResponseEntity<List<SkillResponseDto>> updateSkill(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @RequestBody SkillRequestDto skillRequestDto) {
        Resume resume = getResumeFromUserInfo(userInfo);
        List<SkillResponseDto> updateSkills = skillService.updateSkill(resume, skillRequestDto);
        return ResponseEntity.ok(updateSkills);
    }

    @Operation(
        summary = "스킬 삭제",
        description = "로그인한 사용자가 자신의 특정 스킬을 삭제합니다. "
            + "삭제할 스킬의 ID를 경로 변수로 전달해야 합니다."
    )
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

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}