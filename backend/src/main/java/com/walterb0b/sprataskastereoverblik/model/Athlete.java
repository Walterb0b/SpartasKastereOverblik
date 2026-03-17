package com.walterb0b.sprataskastereoverblik.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "athletes")
public class Athlete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "birth_year", nullable = false)
    private Integer birthYear;

    @Column(name = "statletik_id", nullable = false, unique = true)
    private Integer statletikId;

    public Athlete() {
    }

    public Athlete(String name, Gender gender, Integer birthYear, Integer statletikId) {
        this.name = name;
        this.gender = gender;
        this.birthYear = birthYear;
        this.statletikId = statletikId;
    }
}
