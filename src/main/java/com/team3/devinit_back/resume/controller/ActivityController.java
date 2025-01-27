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

import java.util.List;

@RestController
@RequestMapping("/api/resume/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<List<ActivityResponseDto>> createActivities(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                    @Valid @RequestBody List<ActivityRequestDto> activityRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ActivityResponseDto> activityResponseDtos = activityService.createActivities(resume, activityRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(activityResponseDtos);
    }

    @PatchMapping
    public ResponseEntity<ActivityResponseDto> updateActivities(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @Valid @RequestBody List<ActivityRequestDto> activityRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        activityService.updateActivities(resume, activityRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteActivity(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                 @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        activityService.deleteActivity(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
