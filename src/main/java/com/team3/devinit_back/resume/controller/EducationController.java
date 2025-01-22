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

@RestController
@RequestMapping("/api/educations")
@RequiredArgsConstructor
public class EducationController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final EducationService educationService;

    @PostMapping
    public ResponseEntity<EducationResponseDto> createEducation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                 @Valid @RequestBody EducationRequestDto educationRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        EducationResponseDto educationResponseDto = educationService.createEducation(resume, educationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EducationResponseDto> updateProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @Valid @RequestBody EducationRequestDto educationRequestDto,
                                                              @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        educationService.updateEducation(resume, id, educationRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteEducation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        educationService.deleteEducation(resume,id);
        return ResponseEntity.ok().build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
