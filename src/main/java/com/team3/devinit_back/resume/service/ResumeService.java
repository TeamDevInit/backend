package com.team3.devinit_back.resume.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.*;
import com.team3.devinit_back.resume.entity.*;
import com.team3.devinit_back.resume.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final JPAQueryFactory queryFactory;

    private final ActivityRepository activityRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final LanguageRepository languageRepository;
    private final ProjectRepository projectRepository;

    public Resume findByMemberId(String memberId){ return resumeRepository.findByMemberId(memberId);}
    public ResumeResponseDto getResumeWithDetails(Resume response) {
        QResume resume = QResume.resume;
        QInformation information = QInformation.information;
        QActivity activity = QActivity.activity;
        QEducation education = QEducation.education;
        QExperience experience = QExperience.experience;
        QLanguage language = QLanguage.language;
        QProject project = QProject.project;

        Resume myResume = queryFactory
                .selectFrom(resume)
                .leftJoin(resume.information, information).fetchJoin()
                .leftJoin(resume.activities, activity).fetchJoin()
                .leftJoin(resume.educations, education).fetchJoin()
                .leftJoin(resume.experiences, experience).fetchJoin()
                .leftJoin(resume.languages, language).fetchJoin()
                .leftJoin(resume.projects, project).fetchJoin()
                .where(resume.id.eq(response.getId()))
                .fetchOne();

            if (myResume == null) {
                throw new CustomException(ErrorCode.RESUME_NOT_FOUND);
            }

            return ResumeResponseDto.fromEntity(myResume);
    }




    @Transactional
    public ResumeResponseDto saveOrUpdateResume(Resume resume, ResumeRequestDto resumeRequestDto) {
        Resume existingResume = queryFactory
                .selectFrom(QResume.resume)
                .leftJoin(QResume.resume.activities, QActivity.activity).fetchJoin()
                .leftJoin(QResume.resume.educations, QEducation.education).fetchJoin()
                .leftJoin(QResume.resume.experiences, QExperience.experience).fetchJoin()
                .leftJoin(QResume.resume.languages, QLanguage.language).fetchJoin()
                .leftJoin(QResume.resume.projects, QProject.project).fetchJoin()
                .where(QResume.resume.id.eq(resume.getId()))
                .fetchOne();

        if (existingResume == null) {
            throw new CustomException(ErrorCode.RESUME_NOT_FOUND);
        }

        Set<Activity> updatedActivities = resumeRequestDto.getActivities().stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Activity.builder()
                                .resume(existingResume)
                                .activityName(dto.getActivityName())
                                .organization(dto.getOrganization())
                                .description(dto.getDescription())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else { // ID가 있으면 업데이트
                        return activityRepository.findById(dto.getId())
                                .map(activity -> {
                                    activity.setActivityName(dto.getActivityName());
                                    activity.setOrganization(dto.getOrganization());
                                    activity.setDescription(dto.getDescription());
                                    activity.setStartDate(dto.getStartDate());
                                    activity.setEndDate(dto.getEndDate());
                                    return activity;
                                }).orElseThrow(() -> new IllegalArgumentException("해당 ID의 Activity가 존재하지 않습니다: " + dto.getId()));
                    }
                })
                .collect(Collectors.toSet());

        activityRepository.saveAll(updatedActivities);

        Set<Education> updatedEducations = resumeRequestDto.getEducations().stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Education.builder()
                                .resume(existingResume)
                                .degree(dto.getDegree())
                                .major(dto.getMajor())
                                .organization(dto.getOrganization())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else {
                        return educationRepository.findById(dto.getId())
                                .map(education -> {
                                    education.setDegree(dto.getDegree());
                                    education.setMajor(dto.getMajor());
                                    education.setOrganization(dto.getOrganization());
                                    education.setStartDate(dto.getStartDate());
                                    education.setEndDate(dto.getEndDate());
                                    return education;
                                }).orElseThrow(() -> new IllegalArgumentException("해당 ID의 Education이 존재하지 않습니다: " + dto.getId()));
                    }
                })
                .collect(Collectors.toSet());

        educationRepository.saveAll(updatedEducations);

        Set<Experience> updatedExperiences = resumeRequestDto.getExperiences().stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Experience.builder()
                                .resume(existingResume)
                                .companyName(dto.getCompanyName())
                                .position(dto.getPosition())
                                .description(dto.getDescription())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else {
                        return experienceRepository.findById(dto.getId())
                                .map(experience -> {
                                    experience.setCompanyName(dto.getCompanyName());
                                    experience.setPosition(dto.getPosition());
                                    experience.setDescription(dto.getDescription());
                                    experience.setStartDate(dto.getStartDate());
                                    experience.setEndDate(dto.getEndDate());
                                    return experience;
                                }).orElseThrow(() -> new IllegalArgumentException("해당 ID의 Experience가 존재하지 않습니다: " + dto.getId()));
                    }
                })
                .collect(Collectors.toSet());

        experienceRepository.saveAll(updatedExperiences);

        Set<Project> updatedProjects = resumeRequestDto.getProjects().stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Project.builder()
                                .resume(existingResume)
                                .projectName(dto.getProjectName())
                                .description(dto.getDescription())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else {
                        return projectRepository.findById(dto.getId())
                                .map(project -> {
                                    project.setProjectName(dto.getProjectName());
                                    project.setDescription(dto.getDescription());
                                    project.setStartDate(dto.getStartDate());
                                    project.setEndDate(dto.getEndDate());
                                    return project;
                                }).orElseThrow(() -> new IllegalArgumentException("해당 ID의 Project가 존재하지 않습니다: " + dto.getId()));
                    }
                })
                .collect(Collectors.toSet());

        projectRepository.saveAll(updatedProjects);

        return getResumeWithDetails(existingResume);
    }



}
