package com.walterb0b.sprataskastereoverblik.service;

import com.walterb0b.sprataskastereoverblik.model.Athlete;
import com.walterb0b.sprataskastereoverblik.repository.AthleteRepository;
import com.walterb0b.sprataskastereoverblik.scraper.StatletikScraperService;
import org.springframework.stereotype.Service;



@Service
public class ImportService {

    private final AthleteRepository athleteRepository;
    private final StatletikScraperService scraperService;

    public ImportService(AthleteRepository athleteRepository, StatletikScraperService scraperService) {
        this.athleteRepository = athleteRepository;
        this.scraperService = scraperService;
    }

    public void importAthlete(Long athleteId) {
        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new IllegalArgumentException("Athlete not found: " + athleteId));

        scraperService.importResultsForAthlete(athlete);
    }

    public void importAll() {
        for (Athlete athlete : athleteRepository.findAll()) {
            try {
                scraperService.importResultsForAthlete(athlete);
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Import failed for athlete " + athlete.getId() + ": " + e.getMessage());
            }
        }
    }
}
