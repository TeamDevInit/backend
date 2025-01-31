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
    public ResponseEntity<List<LanguageResponseDto>> createLanguages(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                     @Valid @RequestBody List<LanguageRequestDto> languageRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<LanguageResponseDto> languageResponseDtos = languageService.createLanguages(resume, languageRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(languageResponseDtos);
    }

    @PatchMapping
    public ResponseEntity<LanguageResponseDto> updateLanguages(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                               @Valid @RequestBody List<LanguageRequestDto> languageRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        languageService.updateLanguages(resume, languageRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<List<LanguageResponseDto>> saveOrUpdateLanguages(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                            @Valid @RequestBody List<LanguageRequestDto> languageRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<LanguageResponseDto> languageResponseDtos =  languageService.saveOrUpdateLanguages(resume,languageRequestDtos);

        return ResponseEntity.ok(languageResponseDtos);
    }

    @DeleteMapping("/{id}")
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
