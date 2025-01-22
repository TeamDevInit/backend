package com.team3.devinit_back.resume.controller;

import com.team3.devinit_back.resume.dto.SkillTagResponseDto;
import com.team3.devinit_back.resume.service.SkillTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class SkillTagController {
    private final SkillTagService skillTagService;

    @GetMapping
    public ResponseEntity<List<SkillTagResponseDto>> getAllSkillTags() {
        List<SkillTagResponseDto> skillTags = skillTagService.getAllSkillTags();
        return ResponseEntity.ok(skillTags);
    }
}
