package com.walterb0b.sprataskastereoverblik.dto.result;

public class OverviewRowResponse {
    private String athleteName;
    private Double shotPut;
    private Double discus;
    private Double hammer;
    private Double javelin;
    private Double weightThrow;

    public OverviewRowResponse(String athleteName, Double shotPut, Double discus, Double hammer,
                               Double javelin, Double weightThrow) {
        this.athleteName = athleteName;
        this.shotPut = shotPut;
        this.discus = discus;
        this.hammer = hammer;
        this.javelin = javelin;
        this.weightThrow = weightThrow;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public Double getShotPut() {
        return shotPut;
    }

    public Double getDiscus() {
        return discus;
    }

    public Double getHammer() {
        return hammer;
    }

    public Double getJavelin() {
        return javelin;
    }

    public Double getWeightThrow() {
        return weightThrow;
    }
}
