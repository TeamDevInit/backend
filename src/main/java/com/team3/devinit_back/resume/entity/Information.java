package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Information")
@Getter
@Setter
@NoArgsConstructor
public class Information extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "portfolio", length = 2000)
    private String portfolio;

    @Column(name = "employment_period")
    private String employmentPeriod;

    @OneToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public Information(Long id, String name, String position, String summary, String portfolio,String employmentPeriod, Resume resume) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.summary = summary;
        this.portfolio = portfolio;
        this.employmentPeriod = employmentPeriod;
        this.resume = resume;
    }
}