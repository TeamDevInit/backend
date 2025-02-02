package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.SkillRequestDto;
import com.team3.devinit_back.resume.dto.SkillResponseDto;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.entity.Skill;
import com.team3.devinit_back.resume.entity.SkillTag;
import com.team3.devinit_back.resume.repository.ResumeRepository;
import com.team3.devinit_back.resume.repository.SkillRepository;
import com.team3.devinit_back.resume.repository.SkillTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillService {
    private final ResumeRepository resumeRepository;
    private final SkillRepository skillRepository;
    private final SkillTagRepository skillTagRepository;

    public List<SkillResponseDto> createSkill(Resume resume, SkillRequestDto skillRequestDto) {
        addSkills(resume, skillRequestDto.getSkillNames(), false);
        Resume savedResume = resumeRepository.save(resume);

        return savedResume.getSkills().stream()
                .map(SkillResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SkillResponseDto> updateSkill(Resume resume, SkillRequestDto skillRequestDto) {
        addSkills(resume, skillRequestDto.getSkillNames(), true);
        Resume updatedResume = resumeRepository.save(resume);

        return updatedResume.getSkills().stream()
                .map(SkillResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteSkill(Member member, Long skillId) {
        Skill skill = getSkillById(skillId);
        isAuthorizedForSkill(skill, member);

        skillRepository.delete(skill);
    }

    private void addSkills(Resume resume, List<String> skillNames, boolean isClear){
        if(isClear) {resume.getSkills().clear();}

        skillNames.forEach(skillName -> {
            SkillTag skillTag = getSkillTagByName(skillName);
            Skill skill = new Skill(resume, skillTag);
            resume.getSkills().add(skill);
        });
    }

    private Skill getSkillById(Long skillId) {
        return skillRepository.findById(skillId)
            .orElseThrow(() -> new CustomException(ErrorCode.SKILL_NOT_FOUND));
    }

    private SkillTag getSkillTagByName(String skillName) {
        return skillTagRepository.findByName(skillName)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TAG_ID));
    }

    private void isAuthorizedForResume(Resume resume, Member member) {
        if (!resume.getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void isAuthorizedForSkill(Skill skill, Member member) {
        if (!skill.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

}