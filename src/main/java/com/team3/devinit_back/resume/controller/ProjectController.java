package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.dto.ProjectRequestDto;
import com.team3.devinit_back.resume.dto.ProjectResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.service.ProjectService;
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
@RequestMapping("/api/resume/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ResumeService resumeService;
    private final MemberService memberService;


    @PostMapping
    @Operation(
            summary = "프로젝트 이력 생성",
            description = "사용자의 프로젝트 이력을 리스트형태로 받아 저장하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<List<ProjectResponseDto>> createProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                  @Valid @RequestBody List<ProjectRequestDto> projectRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ProjectResponseDto> projectResponseDtos = projectService.createItems(resume,projectRequestDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponseDtos);
    }

    @PatchMapping
    @Operation(
            summary = "프로젝트 이력 수정",
            description = "사용자의 수정된 프로젝트 이력을 리스트형태로 받아 저장/수정하고 내용을 리스트로 반환 합니다.")
    public ResponseEntity<ProjectResponseDto> updateProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @Valid @RequestBody List<ProjectRequestDto> projectRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        projectService.updateItems(resume, projectRequestDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    @Operation(
            summary = "프로젝트 이력 생성 및 수정",
            description = "사용자의 프로젝트 이력을 리스트형태로 받아 ID가 존재 하지 않는 부분에 대해서는 생성 있는 부분에 대해서는 수정을 진행하고 내역을 반환합니다.")
    public ResponseEntity<List<ProjectResponseDto>> saveOrUpdateProjects(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                            @Valid @RequestBody List<ProjectRequestDto> projectRequestDtos){
        Resume resume = getResumeFromUserInfo(userInfo);
        List<ProjectResponseDto> projectResponseDtos =  projectService.saveOrUpdateItems(resume,projectRequestDtos);

        return ResponseEntity.ok(projectResponseDtos);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "프로젝트 이력 삭제",
            description = "사용자의  프로젝트 이력을 삭제합니다."
                    + "삭제할 프로젝트 이력의 ID를 경로 변수로 전달해야 합니다.")
    public  ResponseEntity<Void> deleteProject(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                               @PathVariable("id") Long id){
        Resume resume = getResumeFromUserInfo(userInfo);
        projectService.deleteItem(resume,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resume getResumeFromUserInfo(CustomOAuth2User userInfo){
        Member member =  memberService.findMemberBySocialId(userInfo.getName());
        return resumeService.findByMemberId(member.getId());
    }
}
