package com.team3.devinit_back.resume.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Skills")
@Getter
@Setter
@NoArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private SkillTag skillTag;

    public Skill(Resume resume, SkillTag skillTag) {
        this.skillTag = skillTag;
        this.resume = resume;
    }
}