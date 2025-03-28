package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Experiences")
@Getter
@Setter
@NoArgsConstructor
public class Experience extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "department")
    private String department;

    @Column(name = "employment_type")
    private String employmentType;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public Experience(Long id, String companyName, String position, LocalDateTime startDate,
                      LocalDateTime endDate, String description, Resume resume,
                      String department, String employmentType) {
        this.id = id;
        this.companyName = companyName;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.resume = resume;
        this.department = department;
        this.employmentType = employmentType;
    }
}