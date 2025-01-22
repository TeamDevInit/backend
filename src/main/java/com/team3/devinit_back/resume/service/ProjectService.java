package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.board.entity.Board;
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
    public ProjectResponseDto createProject(Resume resume, ProjectRequestDto projectRequestDto) {

        Project project = Project.builder()
                .resume(resume)
                .projectName(projectRequestDto.getProjectName())
                .description(projectRequestDto.getDescription())
                .organization(projectRequestDto.getOrganization())
                .endDate(projectRequestDto.getEndDate())
                .startDate(projectRequestDto.getStartDate())
                .build();

        Project savedProject = projectRepository.save(project);

        return ProjectResponseDto.fromEntity(savedProject);
    }

    public List<ProjectResponseDto> getAllProject(Resume resume){
        return projectRepository.findAllByResumeId(resume.getId()).stream()
                .map(ProjectResponseDto::fromEntity)
                .toList();
    }

    public void updateProject(Resume resume, Long id, ProjectRequestDto projectRequestDto) {

        Project project = isAuthorized(id, resume.getMember());
        project.setProjectName(projectRequestDto.getProjectName());
        project.setDescription(projectRequestDto.getDescription());
        project.setOrganization(projectRequestDto.getOrganization());
        project.setStartDate(projectRequestDto.getStartDate());
        project.setStartDate(projectRequestDto.getEndDate());

        projectRepository.save(project);
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
