package com.team3.devinit_back.global.common;


import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.entity.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Activity - Education - Experience - Language - Project 서비스를 위한 탬플릿 패턴
@RequiredArgsConstructor
public abstract class AbstractResumeService<E, ReqDto, ResDto, R extends JpaRepository<E, Long>> {

    protected final R repository;
    protected abstract E toEntity(ReqDto dto, Resume resume);
    protected abstract void updateEntity(E entity, ReqDto dto);
    protected abstract ResDto toResponseDto(E entity);
    protected abstract E getById(Long id);
    protected abstract boolean isDuplicate(Resume resume, ReqDto dto, Long excludeId);
    protected abstract Long getIdFromRequestDto(ReqDto dto);

    @Transactional
    public List<ResDto> createItems(Resume resume, List<ReqDto> requestDtos) {
        requestDtos.forEach(dto -> {
            if (isDuplicate(resume, dto, null)) {
                throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
            }
        });

        List<E> entities = requestDtos.stream()
                .map(dto -> toEntity(dto, resume))
                .toList();

        return repository.saveAll(entities).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public void updateItems(Resume resume, List<ReqDto> requestDtos) {
        requestDtos.forEach(dto -> {
            if (isDuplicate(resume, dto, getIdFromRequestDto(dto))) {
                throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
            }
        });

        List<E> updatedEntities = requestDtos.stream()
                .map(dto -> {
                    E entity = getById(getIdFromRequestDto(dto));
                    updateEntity(entity, dto);
                    return entity;
                })
                .toList();

        repository.saveAll(updatedEntities);
    }

    @Transactional
    public List<ResDto> saveOrUpdateItems(Resume resume, List<ReqDto> requestDtos) {
        requestDtos.forEach(dto -> {
            Long id = getIdFromRequestDto(dto); // DTO에서 ID를 추출
            if (isDuplicate(resume, dto, id)) {
                throw new CustomException(ErrorCode.DUPLICATE_RESOURCE); // 중복된 리소스가 있는 경우 예외 발생
            }
        });
        List<E> entities = requestDtos.stream()
                .map(dto -> {
                    if (getIdFromRequestDto(dto) == null) {
                        return toEntity(dto, resume);
                    } else {
                        E entity = getById(getIdFromRequestDto(dto));

                        updateEntity(entity, dto);
                        return entity;
                    }
                })
                .toList();

        return repository.saveAll(entities).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public void deleteItem(Resume resume, Long id) {
        E entity = getById(id);
        repository.delete(entity);
    }
}