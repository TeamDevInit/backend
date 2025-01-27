package com.team3.devinit_back.resume.controller;


import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.InformationRequestDto;
import com.team3.devinit_back.resume.dto.InformationResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.InformationService;
import com.team3.devinit_back.resume.service.ResumeService;
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
    public ResponseEntity<InformationResponseDto> createInformation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                    @Valid @RequestBody InformationRequestDto informationRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        InformationResponseDto informationResponseDto = informationService.createInformation(resume, informationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(informationResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InformationResponseDto> updateInformation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                    @Valid @RequestBody InformationRequestDto informationRequestDto,
                                                                    @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        informationService.updateInformation(resume, id, informationRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteInformation(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                   @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        informationService.deleteInformation(resume,id);
        return ResponseEntity.ok().build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
