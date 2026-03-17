package com.walterb0b.sprataskastereoverblik.mapper;

import com.walterb0b.sprataskastereoverblik.dto.result.ResultResponse;
import com.walterb0b.sprataskastereoverblik.model.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultMapper {

    public ResultResponse toResponse(Result result) {
        return new ResultResponse(
                result.getId(),
                result.getAthlete().getId(),
                result.getAthlete().getName(),
                result.getDiscipline(),
                result.getResultValue(),
                result.getResultDate(),
                result.getCompetition(),
                result.getLocation()
        );
    }
}