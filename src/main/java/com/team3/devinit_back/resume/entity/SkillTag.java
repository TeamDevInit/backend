package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Skill_Tags")
@Getter
@Setter
@NoArgsConstructor
public class SkillTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "skillTag", cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();

    public SkillTag(String name) { this.name = name; }
}