package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ResumeRequestDto;
import com.team3.devinit_back.resume.dto.ResumeResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    private final MemberService memberService;
    private final ResumeService resumeService;
    @GetMapping("/{memberId}")
    public ResponseEntity<ResumeResponseDto> getCompleteResume(@PathVariable("memberId") String id) {
        Resume resume = resumeService.findByMemberId(id);
        ResumeResponseDto resumeResponseDto = resumeService.getResumeWithDetails(resume);
        return ResponseEntity.ok(resumeResponseDto);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ResumeResponseDto> saveOrUpdateResume(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                @PathVariable("memberId") String memberId,
                                                                @RequestBody ResumeRequestDto resumeRequestDto) {

        Resume resume = resumeService.findByMemberId(memberId);
        ResumeResponseDto responseDto = resumeService.saveOrUpdateResume(resume, resumeRequestDto);
        return ResponseEntity.ok(responseDto);
    }

}
