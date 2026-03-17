package com.walterb0b.sprataskastereoverblik.dto.result;

import com.walterb0b.sprataskastereoverblik.model.Discipline;

import java.time.LocalDate;

public class ResultResponse {

    private Long id;
    private Long athleteId;
    private String athleteName;
    private Discipline discipline;
    private Double resultValue;
    private LocalDate resultDate;
    private String competition;
    private String location;

    public ResultResponse(Long id, Long athleteId, String athleteName, Discipline discipline,
                          Double resultValue, LocalDate resultDate, String competition, String location) {
        this.id = id;
        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.discipline = discipline;
        this.resultValue = resultValue;
        this.resultDate = resultDate;
        this.competition = competition;
        this.location = location;
    }

    public Long getId() { return id; }
    public Long getAthleteId() { return athleteId; }
    public String getAthleteName() { return athleteName; }
    public Discipline getDiscipline() { return discipline; }
    public Double getResultValue() { return resultValue; }
    public LocalDate getResultDate() { return resultDate; }
    public String getCompetition() { return competition; }
    public String getLocation() { return location; }
}