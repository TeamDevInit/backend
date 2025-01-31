package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Language;
import com.team3.devinit_back.resume.entity.Resume;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@RequiredArgsConstructor
public class LanguageResponseDto {

    private Long id;
    private String name;
    private String level;
    private String resumeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LanguageResponseDto(Language language){
        this.id = language.getId();
        this.name = language.getName();
        this.level = language.getLevel();
        Resume resume = language.getResume();
        if(resume!=null){ this.resumeId = resume.getId(); }

        this.createdAt = language.getCreatedAt();
        this.updatedAt = language.getUpdatedAt();
    }

    public static LanguageResponseDto fromEntity(Language language){ return new LanguageResponseDto(language);}
}
