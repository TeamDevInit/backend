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
    public ResponseEntity<List<ExperienceResponseDto>> createExperiences(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                        @Valid @RequestBody List<ExperienceRequestDto> experienceRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ExperienceResponseDto> experienceResponseDtos = experienceService.createExperiences(resume, experienceRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(experienceResponseDtos);

    }

    @PatchMapping
    public ResponseEntity<ExperienceResponseDto> updateExperience(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                  @Valid @RequestBody List<ExperienceRequestDto> experienceRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        experienceService.updateExperiences(resume, experienceRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<List<ExperienceResponseDto>> saveOrUpdateExperiences(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                               @Valid @RequestBody List<ExperienceRequestDto> experienceRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ExperienceResponseDto> experienceResponseDtos =  experienceService.saveOrUpdateExperiences(resume,experienceRequestDtos);

        return ResponseEntity.ok(experienceResponseDtos);
    }

    @DeleteMapping("/{id}")
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
