package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ActivityService;
import com.team3.devinit_back.resume.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponseDto> createActivity(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @Valid @RequestBody ActivityRequestDto activityRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        ActivityResponseDto activityResponseDto = activityService.createActivity(resume, activityRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(activityResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> updateActivity(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @Valid @RequestBody ActivityRequestDto activityRequestDto,
                                                              @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        activityService.updateActivity(resume, id, activityRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteActivity(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        activityService.deleteActivity(resume,id);
        return ResponseEntity.ok().build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
