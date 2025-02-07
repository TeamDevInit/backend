package com.team3.devinit_back.resume.service;


import com.team3.devinit_back.global.common.AbstractResumeService;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.ProjectRequestDto;
import com.team3.devinit_back.resume.dto.ProjectResponseDto;
import com.team3.devinit_back.resume.entity.Project;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends AbstractResumeService<Project, ProjectRequestDto, ProjectResponseDto, ProjectRepository> {

    public ProjectService(ProjectRepository repository) {
        super(repository);
    }
    @Override
    protected Project toEntity(ProjectRequestDto dto, Resume resume) {
        return Project.builder()
                .resume(resume)
                .projectName(dto.getProjectName())
                .description(dto.getDescription())
                .organization(dto.getOrganization())
                .link(dto.getLink())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .build();
    }

    @Override
    protected void updateEntity(Project entity, ProjectRequestDto dto) {
        entity.setProjectName(dto.getProjectName());
        entity.setDescription(dto.getDescription());
        entity.setOrganization(dto.getOrganization());
        entity.setLink(dto.getLink());
        entity.setStartDate(dto.getStartDate());
        entity.setStartDate(dto.getEndDate());
    }

    @Override
    protected ProjectResponseDto toResponseDto(Project entity) {
        return ProjectResponseDto.fromEntity(entity);
    }

    @Override
    protected Project getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    }

    @Override
    protected boolean isDuplicate(Resume resume, ProjectRequestDto dto, Long excludeId) {
        return false;
    }

    @Override
    protected Long getIdFromRequestDto(ProjectRequestDto dto) {
        return dto.getId();
    }

}
