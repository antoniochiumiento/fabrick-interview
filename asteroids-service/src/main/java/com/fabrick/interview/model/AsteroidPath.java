package com.fabrick.interview.model;

import java.time.LocalDate;

public class AsteroidPath {

    private String fromPlanet;
    private String toPlanet;
    private LocalDate fromDate;
    private LocalDate toDate;

    public AsteroidPath() {
    }

    public AsteroidPath(String fromPlanet, String toPlanet, LocalDate fromDate, LocalDate toDate) {
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getFromPlanet() {
        return fromPlanet;
    }

    public void setFromPlanet(String fromPlanet) {
        this.fromPlanet = fromPlanet;
    }

    public String getToPlanet() {
        return toPlanet;
    }

    public void setToPlanet(String toPlanet) {
        this.toPlanet = toPlanet;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
