package com.team3.devinit_back.resume.entity;

import co.elastic.clients.elasticsearch.xpack.usage.Base;
import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Projects")
@Getter
@Setter
@RequiredArgsConstructor
public class Project extends BaseEntity {
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

    @Column(name ="link", length = 2000)
    private String link;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public Project(Long id, String projectName, String description, String organization,
                   LocalDateTime startDate, LocalDateTime endDate, String link, Resume resume) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.organization = organization;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resume = resume;
        this.link = link;
    }
}