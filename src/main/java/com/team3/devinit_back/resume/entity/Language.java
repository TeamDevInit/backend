package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Languages")
@Getter
@Setter
@RequiredArgsConstructor
public class Language extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "level", length = 100)
    private String level;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public Language(Long id, String name, String level, Resume resume) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.resume = resume;
    }
}