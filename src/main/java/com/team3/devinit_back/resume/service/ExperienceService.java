package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.common.AbstractResumeService;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.*;
import com.team3.devinit_back.resume.entity.*;
import com.team3.devinit_back.resume.repository.ExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperienceService extends AbstractResumeService<Experience, ExperienceRequestDto, ExperienceResponseDto, ExperienceRepository> {

    public ExperienceService(ExperienceRepository repository) {
        super(repository);
    }

    @Override
    protected Experience toEntity(ExperienceRequestDto dto, Resume resume) {
        return Experience.builder()
                .resume(resume)
                .companyName(dto.getCompanyName())
                .department(dto.getDepartment())
                .description(dto.getDescription())
                .employmentType(dto.getEmploymentType())
                .position(dto.getPosition())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    @Override
    protected void updateEntity(Experience entity, ExperienceRequestDto dto) {
        entity.setCompanyName(dto.getCompanyName());
        entity.setDepartment(dto.getDepartment());
        entity.setDescription(dto.getDescription());
        entity.setEmploymentType(dto.getEmploymentType());
        entity.setPosition(dto.getPosition());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
    }

    @Override
    protected ExperienceResponseDto toResponseDto(Experience entity) {
        return ExperienceResponseDto.fromEntity(entity);
    }

    @Override
    protected Experience getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));
    }

    @Override
    protected boolean isDuplicate(Resume resume, ExperienceRequestDto dto, Long excludeId) {
        return false;
    }

    @Override
    protected Long getIdFromRequestDto(ExperienceRequestDto dto) {
        return dto.getId();
    }
}


