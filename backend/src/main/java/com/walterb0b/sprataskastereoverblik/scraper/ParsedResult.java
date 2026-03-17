package com.walterb0b.sprataskastereoverblik.scraper;

import com.walterb0b.sprataskastereoverblik.model.Discipline;

import java.time.LocalDate;

public record ParsedResult(
        Discipline discipline,
        double resultValue,
        LocalDate resultDate,
        String competition,
        String location,
        Integer season,
        boolean indoor
) {
}
