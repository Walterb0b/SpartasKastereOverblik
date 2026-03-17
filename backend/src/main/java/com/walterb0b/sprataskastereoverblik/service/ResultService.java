package com.walterb0b.sprataskastereoverblik.service;

import com.walterb0b.sprataskastereoverblik.model.Result;
import com.walterb0b.sprataskastereoverblik.repository.ResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<Result> getAll() {
        return resultRepository.findAll();
    }

    public List<Result> getByAthleteId(Long athleteId) {
        return resultRepository.findByAthleteId(athleteId);
    }

    @Transactional
    public void wipeResults() {
        resultRepository.truncate();
    }
}
