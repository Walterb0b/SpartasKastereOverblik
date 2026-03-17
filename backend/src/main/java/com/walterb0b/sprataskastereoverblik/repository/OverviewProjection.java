package com.walterb0b.sprataskastereoverblik.repository;

public interface OverviewProjection {
    Long getAthleteId();
    String getAthleteName();
    Double getShotPut();
    Double getDiscus();
    Double getHammer();
    Double getJavelin();
    Double getWeightThrow();
}
