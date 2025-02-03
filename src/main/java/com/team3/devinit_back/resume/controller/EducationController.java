package com.team3.devinit_back.resume.controller;


import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.EducationRequestDto;
import com.team3.devinit_back.resume.dto.EducationResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.EducationService;
import com.team3.devinit_back.resume.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume/educations")
@RequiredArgsConstructor
public class EducationController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final EducationService educationService;

    @PostMapping
    public ResponseEntity<List<EducationResponseDto>>createEducations(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                      @Valid @RequestBody List<EducationRequestDto> educationRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<EducationResponseDto> educationResponseDtos = educationService.createEducations(resume, educationRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationResponseDtos);
    }

    @PatchMapping
    public ResponseEntity<EducationResponseDto> updateEducations(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                 @Valid @RequestBody List<EducationRequestDto> educationRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        educationService.updateEducations(resume, educationRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<List<EducationResponseDto>> saveOrUpdateActivities(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                            @Valid @RequestBody List<EducationRequestDto> educationRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<EducationResponseDto> educationResponseDtos =  educationService.saveOrUpdateEducations(resume,educationRequestDtos);

        return ResponseEntity.ok(educationResponseDtos);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteEducation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        educationService.deleteEducation(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
