package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.entity.Skill;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SkillResponseDto {

    private Long id;
    private String name;

    public SkillResponseDto(Skill skill){
        this.id = skill.getId();
        this.name = skill.getSkillTag().getName();
    }
    public static SkillResponseDto fromEntity(Skill skill){ return new SkillResponseDto(skill);}
}