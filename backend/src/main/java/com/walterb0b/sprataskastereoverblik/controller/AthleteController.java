package com.walterb0b.sprataskastereoverblik.controller;

import com.walterb0b.sprataskastereoverblik.dto.athlete.AthleteCreateRequest;
import com.walterb0b.sprataskastereoverblik.dto.athlete.AthleteResponse;
import com.walterb0b.sprataskastereoverblik.dto.athlete.AthleteUpdateRequest;
import com.walterb0b.sprataskastereoverblik.mapper.AthleteMapper;
import com.walterb0b.sprataskastereoverblik.service.AthleteService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/athletes")
public class AthleteController {

    private final AthleteService athleteService;
    private final AthleteMapper athleteMapper;

    public AthleteController(AthleteService athleteService, AthleteMapper athleteMapper) {
        this.athleteService = athleteService;
        this.athleteMapper = athleteMapper;
    }

    @GetMapping
    public List<AthleteResponse> getAll() {
        return athleteService.getAll().stream()
            .map(athleteMapper::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public AthleteResponse getById(@PathVariable Long id) {
        return athleteMapper.toResponse(athleteService.getById(id));
    }

    @PostMapping
    public AthleteResponse create(@Valid @RequestBody AthleteCreateRequest request) {
        return athleteMapper.toResponse(athleteService.create(request));
    }

    @PutMapping("/{id}")
    public AthleteResponse update(@PathVariable Long id, @Valid @RequestBody AthleteUpdateRequest request) {
        return athleteMapper.toResponse(athleteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        athleteService.delete(id);
    }


}
