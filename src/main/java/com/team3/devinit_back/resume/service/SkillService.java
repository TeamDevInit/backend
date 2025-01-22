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

@Service
@RequiredArgsConstructor
@Transactional
public class SkillService {
    private final ResumeRepository resumeRepository;
    private final SkillRepository skillRepository;
    private final SkillTagRepository skillTagRepository;

    public List<SkillResponseDto> addSkill(Member member, SkillRequestDto skillRequestDto) {
        Resume resume = getResumeById(skillRequestDto.getResumeId());
        isAuthorizedForResume(resume, member);

        List<SkillResponseDto> responseDto =new ArrayList<>();

        for (Long skillTagId : skillRequestDto.getSkillTagIds()) {
            SkillTag skillTag = getSkillTagById(skillTagId);
            Skill skill = new Skill(resume, skillTag);
            Skill savedSkill = skillRepository.save(skill);

            responseDto.add(new SkillResponseDto(
                savedSkill.getId(),
                resume.getId(),
                List.of(skillTag.getName())
            ));
        }

        return responseDto;
    }

    public List<SkillResponseDto> updateSkill(Member member, SkillRequestDto skillRequestDto) {
        Resume resume = getResumeById(skillRequestDto.getResumeId());
        isAuthorizedForResume(resume, member);

        List<SkillResponseDto> responseDto = new ArrayList<>();
        List<Skill> updatedSkill = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : skillRequestDto.getSkillTagMap().entrySet()) {
            Long skillId = entry.getKey();
            Long newSkillTagId = entry.getValue();

            Skill skill = getSkillById(skillId);
            isAuthorizedForSkill(skill, member);

            if (skill.getSkillTag().getId().equals(newSkillTagId)) {
                continue;
            }

            SkillTag newSkillTag = getSkillTagById(newSkillTagId);
            skill.setSkillTag(newSkillTag);
            updatedSkill.add(skill);

            responseDto.add(new SkillResponseDto(
                skill.getId(),
                resume.getId(),
                List.of(newSkillTag.getName())
            ));
        }
        skillRepository.saveAll(updatedSkill);

        return responseDto;
    }

    public void deleteSkill(Member member, Long skillId) {
        Skill skill = getSkillById(skillId);
        isAuthorizedForSkill(skill, member);

        skillRepository.delete(skill);
    }

    private Resume getResumeById(String resumeId) {
        return resumeRepository.findById(resumeId)
            .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));
    }

    private Skill getSkillById(Long skillId) {
        return skillRepository.findById(skillId)
            .orElseThrow(() -> new CustomException(ErrorCode.SKILL_NOT_FOUND));
    }

    private SkillTag getSkillTagById(Long skillTagId) {
        return skillTagRepository.findById(skillTagId)
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