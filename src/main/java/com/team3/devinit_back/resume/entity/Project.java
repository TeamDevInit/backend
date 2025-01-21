package com.team3.devinit_back.resume.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Projects")
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String projectName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "organization", length = 100)
    private String organization;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public Project(Long id, String projectName, String description, String organization,
                   LocalDateTime startDate, LocalDateTime endDate, Resume resume) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.organization = organization;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resume = resume;
    }
}