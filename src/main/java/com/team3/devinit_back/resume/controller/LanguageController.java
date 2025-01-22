package com.team3.devinit_back.resume.controller;


import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
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

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;
    private final ResumeService resumeService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<LanguageResponseDto> createLanguage(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @Valid @RequestBody LanguageRequestDto languageRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        LanguageResponseDto languageResponseDto = languageService.createLanguage(resume, languageRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(languageResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LanguageResponseDto> updateLanguage(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @Valid @RequestBody LanguageRequestDto languageRequestDto,
                                                              @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        languageService.updateLanguage(resume, id, languageRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                               @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        languageService.deleteLanguage(resume,id);
        return ResponseEntity.ok().build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
