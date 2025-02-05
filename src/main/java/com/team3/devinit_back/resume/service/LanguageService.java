package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.common.AbstractResumeService;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.*;
import com.team3.devinit_back.resume.entity.Language;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.LanguageRepository;
import org.springframework.stereotype.Service;


@Service
public class LanguageService extends AbstractResumeService<Language, LanguageRequestDto, LanguageResponseDto, LanguageRepository> {

    public LanguageService(LanguageRepository repository) {
        super(repository);
    }

    @Override
    protected Language toEntity(LanguageRequestDto dto, Resume resume){
        return Language.builder()
                .resume(resume)
                .level(dto.getLevel())
                .name(dto.getName())
                .build();
    }
    @Override
    protected void updateEntity(Language entity, LanguageRequestDto dto){
        entity.setName(dto.getName());
        entity.setLevel(dto.getLevel());
    }

    @Override
    protected LanguageResponseDto toResponseDto(Language entity) {
        return LanguageResponseDto.fromEntity(entity);
    }

    @Override
    protected Language getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LANGUAGE_NOT_FOUND));
    }

    @Override
    protected boolean isDuplicate(Resume resume, LanguageRequestDto dto, Long excludeId) {
        repository.findByResumeIdAndName(resume.getId(),dto.getName())
                .filter(language -> !language.getId().equals(excludeId))
                .ifPresent(language -> {
                    throw new CustomException(ErrorCode.DUPLICATE_LANGUAGE);
                });
        return false;
    }

    @Override
    protected Long getIdFromRequestDto(LanguageRequestDto dto) {
        return dto.getId();
    }
}
