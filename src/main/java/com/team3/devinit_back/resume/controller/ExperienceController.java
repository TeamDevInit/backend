package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.dto.ExperienceRequestDto;
import com.team3.devinit_back.resume.dto.ExperienceResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ExperienceService;
import com.team3.devinit_back.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume/experiences")
@RequiredArgsConstructor
public class ExperienceController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final ExperienceService experienceService;

    @PostMapping
    @Operation(
            summary = "경력 사항 생성",
            description = "사용자의 경력 사항을 리스트형태로 받아 저장하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<List<ExperienceResponseDto>> createExperiences(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                        @Valid @RequestBody List<ExperienceRequestDto> experienceRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ExperienceResponseDto> experienceResponseDtos = experienceService.createExperiences(resume, experienceRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(experienceResponseDtos);

    }

    @PatchMapping
    @Operation(
            summary = "경력 사항 수정",
            description = "사용자의 수정된 경력 사항을 리스트형태로 받아 수정하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<ExperienceResponseDto> updateExperience(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                  @Valid @RequestBody List<ExperienceRequestDto> experienceRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        experienceService.updateExperiences(resume, experienceRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    @Operation(
            summary = "경력 사항 생성 및 수정",
            description = "사용자의 경력 사항을 리스트형태로 받아 ID가 존재 하지 않는 부분에 대해서는 생성 있는 부분에 대해서는 수정을 진행하고 내역을 반환합니다.")
    public ResponseEntity<List<ExperienceResponseDto>> saveOrUpdateExperiences(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                               @Valid @RequestBody List<ExperienceRequestDto> experienceRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ExperienceResponseDto> experienceResponseDtos =  experienceService.saveOrUpdateExperiences(resume,experienceRequestDtos);

        return ResponseEntity.ok(experienceResponseDtos);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "경력 사항 삭제",
            description = "사용자의 경력 사항을 삭제합니다."
                    + "삭제할 경력 사항의 ID를 경로 변수로 전달해야 합니다.")
    public  ResponseEntity<Void> deleteExperience(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                  @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        experienceService.deleteExperience(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
