package com.team3.devinit_back.resume.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SkillRequestDto {
    private String resumeId;
    private List<Long> skillTagIds;
    private Map<Long, Long> skillTagMap;
}