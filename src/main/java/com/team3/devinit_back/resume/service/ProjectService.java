package com.team3.devinit_back.resume.service;


import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.ProjectRequestDto;
import com.team3.devinit_back.resume.dto.ProjectResponseDto;
import com.team3.devinit_back.resume.entity.Project;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public List<ProjectResponseDto> createProjects(Resume resume, List<ProjectRequestDto> projectRequestDtos) {

        List<Project> projects = projectRequestDtos.stream()
                .map(projectRequestDto -> Project.builder()
                    .resume(resume)
                    .projectName(projectRequestDto.getProjectName())
                    .description(projectRequestDto.getDescription())
                    .organization(projectRequestDto.getOrganization())
                    .endDate(projectRequestDto.getEndDate())
                    .startDate(projectRequestDto.getStartDate())
                    .build())
                .toList();

        List<Project> savedProjects = projectRepository.saveAll(projects);

        return savedProjects.stream()
                .map(ProjectResponseDto::fromEntity)
                .toList();
    }

    public List<ProjectResponseDto> getAllProject(Resume resume){
        return projectRepository.findAllByResumeId(resume.getId()).stream()
                .map(ProjectResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateProjects(Resume resume, List<ProjectRequestDto> projectRequestDtos) {

        List<Project> updatedProjects = projectRequestDtos.stream()
                .map(projectRequestDto -> {
                    Long id = projectRequestDto.getId();
                    Project project = isAuthorized(id, resume.getMember());
                    project.setProjectName(projectRequestDto.getProjectName());
                    project.setDescription(projectRequestDto.getDescription());
                    project.setOrganization(projectRequestDto.getOrganization());
                    project.setStartDate(projectRequestDto.getStartDate());
                    project.setStartDate(projectRequestDto.getEndDate());
                    return project;
                })
                .toList();

        projectRepository.saveAll(updatedProjects);
    }

    @Transactional
    public List<ProjectResponseDto> saveOrUpdateProjects(Resume resume,  List<ProjectRequestDto> projectRequestDtos) {
        List<Project> projects = projectRequestDtos.stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Project.builder()
                                .resume(resume)
                                .projectName(dto.getProjectName())
                                .description(dto.getDescription())
                                .organization(dto.getOrganization())
                                .endDate(dto.getEndDate())
                                .startDate(dto.getStartDate())
                                .build();
                    } else {
                        return projectRepository.findById(dto.getId())
                                .map(project -> {
                                    project.setProjectName(dto.getProjectName());
                                    project.setDescription(dto.getDescription());
                                    project.setOrganization(dto.getOrganization());
                                    project.setStartDate(dto.getStartDate());
                                    project.setStartDate(dto.getEndDate());
                                    return project;
                                }).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
                    }
                })
                .toList();

        // DB에 저장
        List<Project> savedProjects = projectRepository.saveAll(projects);

        // DTO 변환 후 반환
        return savedProjects.stream()
                .map(ProjectResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteProject(Resume resume, Long id) {
        Project project = isAuthorized(id, resume.getMember());
        projectRepository.delete(project);
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    }

    private Project isAuthorized(Long id, Member member) {
        Project project =  getProjectById(id);
        if (!project.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return project;
    }
}
