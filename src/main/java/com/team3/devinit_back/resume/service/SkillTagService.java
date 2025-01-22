package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.resume.dto.SkillTagResponseDto;
import com.team3.devinit_back.resume.repository.SkillTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillTagService {
    private final SkillTagRepository skillTagRepository;

    public List<SkillTagResponseDto> getAllSkillTags() {
        return skillTagRepository.findAll().stream()
            .map(skillTag -> new SkillTagResponseDto(skillTag.getId(), skillTag.getName()))
            .collect(Collectors.toList());
    }
}