package com.walterb0b.sprataskastereoverblik.service;

import com.walterb0b.sprataskastereoverblik.dto.overview.OverviewRowResponse;
import com.walterb0b.sprataskastereoverblik.repository.OverviewProjection;
import com.walterb0b.sprataskastereoverblik.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewService {

    private final ResultRepository resultRepository;

    public OverviewService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<OverviewRowResponse> getPROverview() {
        return resultRepository.getPrOverview()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<OverviewRowResponse> getSeasonOverview(int year) {
        return resultRepository.getSbOverview(year)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private OverviewRowResponse toDto(OverviewProjection p) {
        return new OverviewRowResponse(
                p.getAthleteId(),
                p.getAthleteName(),
                p.getShotPut(),
                p.getDiscus(),
                p.getHammer(),
                p.getJavelin(),
                p.getWeightThrow()
        );
    }
}