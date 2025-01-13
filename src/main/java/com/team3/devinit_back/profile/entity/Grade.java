package com.team3.devinit_back.profile.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;
}
