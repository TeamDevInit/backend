package com.team3.devinit_back.resume.controller;


import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.EducationRequestDto;
import com.team3.devinit_back.resume.dto.EducationResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.EducationService;
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
@RequestMapping("/api/resume/educations")
@RequiredArgsConstructor
public class EducationController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final EducationService educationService;

    @PostMapping
    @Operation(
            summary = "교육 이력 생성",
            description = "사용자의 교육 이력을 리스트형태로 받아 저장하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<List<EducationResponseDto>>createEducations(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                      @Valid @RequestBody List<EducationRequestDto> educationRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<EducationResponseDto> educationResponseDtos = educationService.createItems(resume, educationRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationResponseDtos);
    }

    @PatchMapping
    @Operation(
            summary = "교육 이력 수정",
            description = "사용자의 수정된 교육 이력을 리스트형태로 받아 수정하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<EducationResponseDto> updateEducations(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                 @Valid @RequestBody List<EducationRequestDto> educationRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        educationService.updateItems(resume, educationRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    @Operation(
            summary = "교육 이력 생성 및 수정",
            description = "사용자의 교육 이력을 리스트형태로 받아 ID가 존재 하지 않는 부분에 대해서는 생성 있는 부분에 대해서는 수정을 진행하고 내역을 반환합니다.")
    public ResponseEntity<List<EducationResponseDto>> saveOrUpdateActivities(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                            @Valid @RequestBody List<EducationRequestDto> educationRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<EducationResponseDto> educationResponseDtos =  educationService.saveOrUpdateItems(resume,educationRequestDtos);

        return ResponseEntity.ok(educationResponseDtos);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "교육 이력 삭제",
            description = "사용자의 교육 이력을 삭제합니다."
                    + "삭제할 교육 이력의 ID를 경로 변수로 전달해야 합니다.")
    public  ResponseEntity<Void> deleteEducation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        educationService.deleteItem(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
