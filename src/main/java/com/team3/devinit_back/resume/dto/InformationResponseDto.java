package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Information;
import com.team3.devinit_back.resume.entity.Resume;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@RequiredArgsConstructor
public class InformationResponseDto {
    private Long id;
    private String name;
    private String position;
    private String summary;
    private String portfolio;
    private String resumeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InformationResponseDto(Information information){
        this.id = information.getId();
        this.name = information.getName();
        this.position = information.getPosition();
        this.portfolio = information.getPortfolio();
        this.summary = information.getSummary();

        Resume resume = information.getResume();
        if(resume != null) {this.resumeId = resume.getId();}

        this.createdAt = information.getCreatedAt();
        this.updatedAt = information.getUpdatedAt();
    }
    public static InformationResponseDto fromEntity(Information information) { return new InformationResponseDto(information);}
}
