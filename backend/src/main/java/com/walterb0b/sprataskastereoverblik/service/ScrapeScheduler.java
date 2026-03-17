package com.walterb0b.sprataskastereoverblik.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScrapeScheduler {

    private final ImportService importService;

    public ScrapeScheduler(ImportService importService) {
        this.importService = importService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void scrapeAllAthletes() {
        System.out.println("Starting scheduled scrape of all athletes...");

        importService.importAll();
    }
}
