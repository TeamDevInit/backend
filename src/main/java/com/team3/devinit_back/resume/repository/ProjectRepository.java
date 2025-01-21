package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByResumeId(String id);
}
