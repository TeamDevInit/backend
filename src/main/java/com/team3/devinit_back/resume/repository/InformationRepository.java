package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.dto.InformationResponseDto;
import com.team3.devinit_back.resume.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {
    Information findByResumeId(String id);
}
