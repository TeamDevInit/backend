package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.resume.dto.SkillTagResponseDto;
import com.team3.devinit_back.resume.service.SkillTagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resume/tags")
@RequiredArgsConstructor
public class SkillTagController {
    private final SkillTagService skillTagService;

    @Operation(
        summary = "기술스택 태그 조회",
        description = "시스템에 등록된 모든 기술스택 태그를 조회합니다. "
            + "태그는 검색이나 추천 용도로 사용될 수 있습니다."
    )
    @GetMapping
    public ResponseEntity<List<SkillTagResponseDto>> getAllSkillTags() {
        List<SkillTagResponseDto> skillTags = skillTagService.getAllSkillTags();
        return ResponseEntity.ok(skillTags);
    }
}