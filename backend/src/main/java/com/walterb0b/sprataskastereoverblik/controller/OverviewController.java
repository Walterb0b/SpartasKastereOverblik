package com.walterb0b.sprataskastereoverblik.controller;

import com.walterb0b.sprataskastereoverblik.dto.overview.OverviewRowResponse;
import com.walterb0b.sprataskastereoverblik.service.OverviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/overview")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @GetMapping("/pr")
    public List<OverviewRowResponse> getPROverview() {
        return overviewService.getPROverview();
    }

    @GetMapping("/sb/{year}")
    public List<OverviewRowResponse> getSeasonOverview(@PathVariable int year) {
        return overviewService.getSeasonOverview(year);
    }
}