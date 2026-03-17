package com.walterb0b.sprataskastereoverblik.service;

import com.walterb0b.sprataskastereoverblik.dto.athlete.AthleteCreateRequest;
import com.walterb0b.sprataskastereoverblik.dto.athlete.AthleteUpdateRequest;
import com.walterb0b.sprataskastereoverblik.model.Athlete;
import com.walterb0b.sprataskastereoverblik.repository.AthleteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;

    public AthleteService(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    public List<Athlete> getAll() {
        return athleteRepository.findAll();
    }

    public Athlete getById(Long id) {
        return athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete not found with id: " + id));
    }

    public Athlete create(AthleteCreateRequest request) {
        Athlete athlete = new Athlete(
            request.getName(),
            request.getGender(),
            request.getBirthYear(),
            request.getStatletikId()
        );
        return athleteRepository.save(athlete);
    }

    public Athlete update(Long id, AthleteUpdateRequest request) {
        Athlete athlete = getById(id);
        athlete.setName(request.getName());
        athlete.setGender(request.getGender());
        athlete.setBirthYear(request.getBirthYear());
        athlete.setStatletikId(request.getStatletikId());
        return athleteRepository.save(athlete);
    }

    public void delete(Long id) {
        Athlete athlete = getById(id);
        athleteRepository.delete(athlete);
    }
}
