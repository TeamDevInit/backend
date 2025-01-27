package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findAllByResumeId(String id);
}
