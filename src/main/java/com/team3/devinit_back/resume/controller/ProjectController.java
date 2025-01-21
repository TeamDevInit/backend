package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ProjectRequestDto;
import com.team3.devinit_back.resume.dto.ProjectResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ProjectService;
import com.team3.devinit_back.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@Slf4j
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ResumeService resumeService;
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody ProjectRequestDto projectRequestDto){
        Resume resume = getResumeFromUserInfo(userInfo);
        ProjectResponseDto projectResponseDto = projectService.createProject(resume,projectRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody ProjectRequestDto projectRequestDto,
                                                            @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        projectService.updateProject(resume, id, projectRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                               @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        projectService.deleteProject(resume,id);
        return ResponseEntity.ok().build();
    }




    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }


    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
