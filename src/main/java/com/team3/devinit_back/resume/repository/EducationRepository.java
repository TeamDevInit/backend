package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education,Long> {
    List<Education> findAllByResumeId(String id);

    Optional<Education> findByResumeIdAndOrganizationAndDegreeAndMajor(String resumeId, String organization, String degree, String major);
}
