package com.walterb0b.sprataskastereoverblik.controller;

import com.walterb0b.sprataskastereoverblik.dto.importjob.ImportResponse;
import com.walterb0b.sprataskastereoverblik.service.ImportService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/athlete/{athleteId}")
    public ImportResponse importAthleteData(@PathVariable Long athleteId) {
        importService.importAthlete(athleteId);
        return new ImportResponse("Import completed for athlete with id: " + athleteId);
    }

    @PostMapping("/all")
    public ImportResponse importAll() {
        importService.importAll();
        return new ImportResponse("Import completed for all athletes");
    }
}
