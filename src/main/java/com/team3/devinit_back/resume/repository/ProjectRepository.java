package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByResumeId(String id);
}
