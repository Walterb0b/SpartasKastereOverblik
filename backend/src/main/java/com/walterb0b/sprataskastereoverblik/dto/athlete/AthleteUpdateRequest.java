package com.walterb0b.sprataskastereoverblik.dto.athlete;

import com.walterb0b.sprataskastereoverblik.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AthleteUpdateRequest {
    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @NotNull
    private Integer birthYear;

    @NotNull
    private Integer statletikId;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public void setStatletikId(Integer statletikId) {
        this.statletikId = statletikId;
    }
}
