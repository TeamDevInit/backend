package com.team3.devinit_back.resume.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
public class SkillRequestDto {
    private List<String> skillNames;
}