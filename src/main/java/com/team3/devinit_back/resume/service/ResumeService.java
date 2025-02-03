package com.team3.devinit_back.resume.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.ResumeRequestDto;
import com.team3.devinit_back.resume.dto.ResumeResponseDto;
import com.team3.devinit_back.resume.entity.*;
import com.team3.devinit_back.resume.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static com.team3.devinit_back.resume.entity.QActivity.activity;
import static com.team3.devinit_back.resume.entity.QEducation.education;
import static com.team3.devinit_back.resume.entity.QExperience.experience;
import static com.team3.devinit_back.resume.entity.QLanguage.language;
import static com.team3.devinit_back.resume.entity.QProject.project;
import static com.team3.devinit_back.resume.entity.QResume.resume;

@Service
@RequiredArgsConstructor
public class ResumeService {
    @PersistenceContext
    private EntityManager em;
    private final ResumeRepository resumeRepository;
    private final JPAQueryFactory queryFactory;

    private final ActivityRepository activityRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final LanguageRepository languageRepository;
    private final ProjectRepository projectRepository;

    public Resume findByMemberId(String memberId) {
        return resumeRepository.findByMemberId(memberId);
    }

    public ResumeResponseDto getResumeWithDetails(Resume response) {
        Resume myResume = queryFactory
                .selectFrom(resume)
                .leftJoin(resume.information, QInformation.information).fetchJoin()
                .leftJoin(resume.skills, QSkill.skill).fetchJoin()
                .leftJoin(resume.activities, activity).fetchJoin()
                .leftJoin(resume.educations, education).fetchJoin()
                .leftJoin(resume.experiences, experience).fetchJoin()
                .leftJoin(resume.languages, language).fetchJoin()
                .leftJoin(resume.projects, project).fetchJoin()
                .where(resume.id.eq(response.getId()))
                .distinct()
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
                .leftJoin(QResume.resume.activities, activity).fetchJoin()
                .leftJoin(QResume.resume.educations, education).fetchJoin()
                .leftJoin(QResume.resume.experiences, experience).fetchJoin()
                .leftJoin(QResume.resume.languages, language).fetchJoin()
                .leftJoin(QResume.resume.projects, project).fetchJoin()
                .where(QResume.resume.id.eq(resume.getId()))
                .distinct()
                .fetchOne();

        if (existingResume == null) {
            throw new CustomException(ErrorCode.RESUME_NOT_FOUND);
        }

        updateActivities(existingResume, resumeRequestDto);
        updateEducations(existingResume, resumeRequestDto);
        updateExperiences(existingResume, resumeRequestDto);
        updateLanguages(existingResume, resumeRequestDto);
        updateProjects(existingResume, resumeRequestDto);
        em.flush();
        em.clear();

        return getResumeWithDetails(existingResume);
    }

    private void updateActivities(Resume existingResume, ResumeRequestDto dto) {
        Set<Activity> updatedActivities = dto.getActivities().stream()
                .map(actDto -> {
                    if (actDto.getId() == null) {
                        return Activity.builder()
                                .resume(existingResume)
                                .activityName(actDto.getActivityName())
                                .organization(actDto.getOrganization())
                                .description(actDto.getDescription())
                                .startDate(actDto.getStartDate())
                                .endDate(actDto.getEndDate())
                                .build();
                    } else {
                        return activityRepository.findById(actDto.getId())
                                .map(activity -> {
                                    activity.setActivityName(actDto.getActivityName());
                                    activity.setOrganization(actDto.getOrganization());
                                    activity.setDescription(actDto.getDescription());
                                    activity.setStartDate(actDto.getStartDate());
                                    activity.setEndDate(actDto.getEndDate());
                                    return activity;
                                }).orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
                    }
                })
                .collect(Collectors.toSet());
        activityRepository.saveAll(updatedActivities);
    }

    private void updateEducations(Resume existingResume, ResumeRequestDto dto) {
        Set<Education> updatedEducations = dto.getEducations().stream()
                .map(eduDto -> {
                    if (eduDto.getId() == null) {
                        return Education.builder()
                                .resume(existingResume)
                                .degree(eduDto.getDegree())
                                .major(eduDto.getMajor())
                                .organization(eduDto.getOrganization())
                                .startDate(eduDto.getStartDate())
                                .endDate(eduDto.getEndDate())
                                .build();
                    } else {
                        return educationRepository.findById(eduDto.getId())
                                .map(education -> {
                                    education.setDegree(eduDto.getDegree());
                                    education.setMajor(eduDto.getMajor());
                                    education.setOrganization(eduDto.getOrganization());
                                    education.setStartDate(eduDto.getStartDate());
                                    education.setEndDate(eduDto.getEndDate());
                                    return education;
                                }).orElseThrow(() -> new CustomException(ErrorCode.EDUCATION_NOT_FOUND));
                    }
                })
                .collect(Collectors.toSet());
        educationRepository.saveAll(updatedEducations);
    }

    private void updateExperiences(Resume existingResume, ResumeRequestDto dto) {
        Set<Experience> updatedExperiences = dto.getExperiences().stream()
                .map(expDto -> {
                    if (expDto.getId() == null) {
                        return Experience.builder()
                                .resume(existingResume)
                                .companyName(expDto.getCompanyName())
                                .position(expDto.getPosition())
                                .description(expDto.getDescription())
                                .startDate(expDto.getStartDate())
                                .endDate(expDto.getEndDate())
                                .build();
                    } else {
                        return experienceRepository.findById(expDto.getId())
                                .map(exp -> {
                                    exp.setCompanyName(expDto.getCompanyName());
                                    exp.setPosition(expDto.getPosition());
                                    exp.setDescription(expDto.getDescription());
                                    exp.setStartDate(expDto.getStartDate());
                                    exp.setEndDate(expDto.getEndDate());
                                    return exp;
                                }).orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));
                    }
                })
                .collect(Collectors.toSet());
        experienceRepository.saveAll(updatedExperiences);
    }

    private void updateLanguages(Resume existingResume, ResumeRequestDto dto) {
        Set<Language> updatedLanguages = dto.getLanguages().stream()
                .map(langDto -> {
                    if (langDto.getId() == null) {
                        return Language.builder()
                                .resume(existingResume)
                                .name(langDto.getName())
                                .level(langDto.getLevel())
                                .build();
                    } else {
                        return languageRepository.findById(langDto.getId())
                                .map(lang -> {
                                    lang.setName(langDto.getName());
                                    lang.setLevel(langDto.getLevel());
                                    return lang;
                                }).orElseThrow(() -> new CustomException(ErrorCode.LANGUAGE_NOT_FOUND));
                    }
                })
                .collect(Collectors.toSet());
        languageRepository.saveAll(updatedLanguages);
    }

    private void updateProjects(Resume existingResume, ResumeRequestDto dto) {
        Set<Project> updatedProjects = dto.getProjects().stream()
                .map(projDto -> {
                    if (projDto.getId() == null) {
                        return Project.builder()
                                .resume(existingResume)
                                .projectName(projDto.getProjectName())
                                .description(projDto.getDescription())
                                .startDate(projDto.getStartDate())
                                .endDate(projDto.getEndDate())
                                .build();
                    } else {
                        return projectRepository.findById(projDto.getId())
                                .map(proj -> {
                                    proj.setProjectName(projDto.getProjectName());
                                    proj.setDescription(projDto.getDescription());
                                    proj.setStartDate(projDto.getStartDate());
                                    proj.setEndDate(projDto.getEndDate());
                                    return proj;
                                }).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
                    }
                })
                .collect(Collectors.toSet());
        projectRepository.saveAll(updatedProjects);
    }
}
