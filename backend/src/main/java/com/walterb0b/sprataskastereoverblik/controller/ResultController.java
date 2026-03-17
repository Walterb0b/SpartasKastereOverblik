package com.walterb0b.sprataskastereoverblik.controller;

import com.walterb0b.sprataskastereoverblik.dto.result.ResultResponse;
import com.walterb0b.sprataskastereoverblik.mapper.ResultMapper;
import com.walterb0b.sprataskastereoverblik.service.ResultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;
    private final ResultMapper resultMapper;

    public ResultController(ResultService resultService, ResultMapper resultMapper) {
        this.resultService = resultService;
        this.resultMapper = resultMapper;
    }

    @GetMapping
    public List<ResultResponse> getAllResults() {
        return resultService.getAll().stream()
            .map(resultMapper::toResponse)
            .toList();
    }


    @GetMapping("/athlete/{athleteId}")
    public List<ResultResponse> getByAthlete(@PathVariable Long athleteId) {
        return resultService.getByAthleteId(athleteId).stream()
            .map(resultMapper::toResponse)
            .toList();
    }

}
