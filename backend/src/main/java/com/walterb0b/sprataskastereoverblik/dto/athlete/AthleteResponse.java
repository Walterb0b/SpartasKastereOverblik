package com.walterb0b.sprataskastereoverblik.dto.athlete;

import com.walterb0b.sprataskastereoverblik.model.Gender;

public class AthleteResponse {
    private Long id;
    private String name;
    private Gender gender;
    private Integer birthYear;
    private Integer statletikId;

    public AthleteResponse(Long id, String name, Gender gender, Integer birthYear, Integer statletikId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthYear = birthYear;
        this.statletikId = statletikId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getStatletikId() {
        return statletikId;
    }
}
