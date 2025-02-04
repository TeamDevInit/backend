package com.team3.devinit_back.resume.controller;


import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.InformationRequestDto;
import com.team3.devinit_back.resume.dto.InformationResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.InformationService;
import com.team3.devinit_back.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume/information")
@RequiredArgsConstructor
public class InformationController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final InformationService informationService;

    @PostMapping
    @Operation(
            summary = "기본 정보 생성",
            description = "사용자의 기본 정보을 받아 저장하고 내용을 반환 합니다.")
    public ResponseEntity<InformationResponseDto> createInformation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                    @Valid @RequestBody InformationRequestDto informationRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        InformationResponseDto informationResponseDto = informationService.createInformation(resume, informationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(informationResponseDto);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "기본 정보 수정",
            description = "사용자의 수정된 기본 정보을 받아 수정하고 내용을 반환 합니다.")
    public ResponseEntity<InformationResponseDto> updateInformation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                    @Valid @RequestBody InformationRequestDto informationRequestDto,
                                                                    @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        informationService.updateInformation(resume, id, informationRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "기본 정보 삭제",
            description = "사용자의 기본 정보를 삭제합니다."
                    + "삭제할 기본정보의 ID를 경로 변수로 전달해야 합니다.")
    public  ResponseEntity<Void> deleteInformation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                   @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        informationService.deleteInformation(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
