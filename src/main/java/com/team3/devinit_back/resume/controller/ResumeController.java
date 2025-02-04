package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ResumeRequestDto;
import com.team3.devinit_back.resume.dto.ResumeResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "이력서 상세 조회",
            description = "사용자의 이력서 정보를 상제 조회합니다. "
                         + "조회할 이력서의 사용자 ID를 경로 변수로 전달해야 합니다.")
    public ResponseEntity<ResumeResponseDto> getCompleteResume(@PathVariable("memberId") String id) {
        Resume resume = resumeService.findByMemberId(id);
        ResumeResponseDto resumeResponseDto = resumeService.getResumeWithDetails(resume);
        return ResponseEntity.ok(resumeResponseDto);
    }

    @PutMapping("/{memberId}")
    @Operation(
            summary = "이력서 전체저장 및 수정",
            description = "로그인한 사용자의 이력서의 작성 정보를 전체 갱신 및 저장합니다. "
                         + "갱신할 이력서의 사용자 ID를 경로 변수로 전달해야 합니다.")
    public ResponseEntity<ResumeResponseDto> saveOrUpdateResume(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                @PathVariable("memberId") String memberId,
                                                                @RequestBody ResumeRequestDto resumeRequestDto) {

        Resume resume = resumeService.findByMemberId(memberId);
        ResumeResponseDto responseDto = resumeService.saveOrUpdateResume(resume, resumeRequestDto);
        return ResponseEntity.ok(responseDto);
    }

}
