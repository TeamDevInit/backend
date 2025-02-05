package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.common.AbstractResumeService;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.EducationRequestDto;
import com.team3.devinit_back.resume.dto.EducationResponseDto;
import com.team3.devinit_back.resume.entity.Education;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.EducationRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationService extends AbstractResumeService<Education, EducationRequestDto, EducationResponseDto, EducationRepository> {

    public EducationService(EducationRepository repository) {
        super(repository);
    }

    @Override
    protected Education toEntity(EducationRequestDto  dto, Resume resume) {
        return Education.builder()
                .resume(resume)
                .organization(dto.getOrganization())
                .degree(dto.getDegree())
                .major(dto.getMajor())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .build();
    }

    @Override
    protected void updateEntity(Education entity, EducationRequestDto dto) {
        entity.setOrganization(dto.getOrganization());
        entity.setDegree(dto.getDegree());
        entity.setMajor(dto.getMajor());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setStatus(dto.getStatus());
    }

    @Override
    protected EducationResponseDto toResponseDto(Education  entity) {
        return EducationResponseDto.fromEntity(entity);
    }

    @Override
    protected Education getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EDUCATION_NOT_FOUND));
    }

    @Override
    protected boolean isDuplicate(Resume resume, EducationRequestDto dto, Long excludeId) {
        return repository.findByResumeIdAndOrganizationAndDegreeAndMajor(
                        resume.getId(), dto.getOrganization(), dto.getDegree(), dto.getMajor())
                .filter(education -> excludeId == null || !education.getId().equals(excludeId))
                .isPresent();
    }

    @Override
    protected Long getIdFromRequestDto(EducationRequestDto  dto) {
        return dto.getId();
    }
}