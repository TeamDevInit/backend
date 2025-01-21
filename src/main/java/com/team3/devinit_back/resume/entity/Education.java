package com.team3.devinit_back.resume.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Educations")
@Getter
@Setter
@NoArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization", length = 100)
    private String organization;

    @Column(name = "degree", length = 100)
    private String degree;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public Education(Long id, String organization, String degree, String major,
                     LocalDateTime startDate, LocalDateTime endDate, String status, Resume resume) {
        this.id = id;
        this.organization = organization;
        this.degree = degree;
        this.major = major;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.resume = resume;
    }
}