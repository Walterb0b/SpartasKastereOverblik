package com.walterb0b.sprataskastereoverblik.mapper;

import com.walterb0b.sprataskastereoverblik.dto.athlete.AthleteResponse;
import com.walterb0b.sprataskastereoverblik.model.Athlete;
import org.springframework.stereotype.Component;

@Component
public class AthleteMapper {

    public AthleteResponse toResponse(Athlete athlete) {
        return new AthleteResponse(
            athlete.getId(),
            athlete.getName(),
            athlete.getGender(),
            athlete.getBirthYear(),
            athlete.getStatletikId()
        );
    }
}
