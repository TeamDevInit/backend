package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ProjectRequestDto;
import com.team3.devinit_back.resume.dto.ProjectResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ProjectService;
import com.team3.devinit_back.resume.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ResumeService resumeService;
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<List<ProjectResponseDto>> createProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                  @Valid @RequestBody List<ProjectRequestDto> projectRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ProjectResponseDto> projectResponseDtos = projectService.createProjects(resume,projectRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponseDtos);
    }

    @PatchMapping
    public ResponseEntity<ProjectResponseDto> updateProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @Valid @RequestBody List<ProjectRequestDto> projectRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        projectService.updateProjects(resume, projectRequestDtos);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                               @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        projectService.deleteProject(resume,id);
        return ResponseEntity.ok().build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
