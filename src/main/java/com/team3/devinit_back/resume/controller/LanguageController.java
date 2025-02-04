package com.team3.devinit_back.resume.controller;


import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.dto.LanguageRequestDto;
import com.team3.devinit_back.resume.dto.LanguageResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.LanguageService;
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
@RequestMapping("/api/resume/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;
    private final ResumeService resumeService;
    private final MemberService memberService;

    @PostMapping
    @Operation(
            summary = "어학 이력 생성",
            description = "사용자의 어학 이력을 리스트형태로 받아 저장하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<List<LanguageResponseDto>> createLanguages(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                     @Valid @RequestBody List<LanguageRequestDto> languageRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<LanguageResponseDto> languageResponseDtos = languageService.createLanguages(resume, languageRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(languageResponseDtos);
    }

    @PatchMapping
    @Operation(
            summary = "어학 이력 수정",
            description = "사용자의 수정된 어학 이력을 리스트형태로 받아 수정하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<LanguageResponseDto> updateLanguages(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                               @Valid @RequestBody List<LanguageRequestDto> languageRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        languageService.updateLanguages(resume, languageRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    @Operation(
            summary = "어학 이력 생성 및 수정",
            description = "사용자의 어학 이력을 리스트형태로 받아 ID가 존재 하지 않는 부분에 대해서는 생성 있는 부분에 대해서는 수정을 진행하고 내역을 반환합니다.")
    public ResponseEntity<List<LanguageResponseDto>> saveOrUpdateLanguages(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                            @Valid @RequestBody List<LanguageRequestDto> languageRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<LanguageResponseDto> languageResponseDtos =  languageService.saveOrUpdateLanguages(resume,languageRequestDtos);

        return ResponseEntity.ok(languageResponseDtos);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "어학 이력 삭제",
            description = "사용자의 어학 이력을 삭제합니다."
                    + "삭제할 어학 이력의 ID를 경로 변수로 전달해야 합니다.")
    public ResponseEntity<Void> deleteLanguage(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                               @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        languageService.deleteLanguage(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
