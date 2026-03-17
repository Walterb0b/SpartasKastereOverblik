package com.walterb0b.sprataskastereoverblik.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(
        name = "results",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "athlete_id", "discipline", "result_date", "result_value"
                })
        }
)
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Discipline discipline;

    @Column(name = "result_value", nullable = false)
    private double resultValue;

    @Column(name = "result_date", nullable = false)
    private LocalDate resultDate;

    @Column(name = "competition")
    private String competition;

    @Column(name = "location")
    private String location;
}
