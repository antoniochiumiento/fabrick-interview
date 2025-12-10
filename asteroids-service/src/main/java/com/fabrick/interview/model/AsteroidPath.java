package com.fabrick.interview.model;

import java.time.LocalDate;

/**
 * Represents a segment of an asteroid's journey across the solar system.
 * <p>
 * This model captures the movement of an asteroid from one celestial body to another
 * within a specific time range. It serves as the main output object for the
 * Asteroid Path API.
 * </p>
 */

public class AsteroidPath {

    /**
     * The celestial body (e.g., "Earth", "Juptr") where the path segment begins.
     */
    private String fromPlanet;

    /**
     * The celestial body (e.g., "Mars", "Venus") where the path segment ends.
     */
    private String toPlanet;

    /**
     * The date when the asteroid was first detected near the {@code fromPlanet}.
     */
    private LocalDate fromDate;

    /**
     * The date when the asteroid arrives near the {@code toPlanet}.
     */
    private LocalDate toDate;

    /**
     * Default constructor required for serialization/deserialization frameworks.
     */
    public AsteroidPath() {
    }

    /**
     * Constructs a new AsteroidPath with specific details.
     *
     * @param fromPlanet The starting celestial body.
     * @param toPlanet   The destination celestial body.
     * @param fromDate   The start date of the journey segment.
     * @param toDate     The end date of the journey segment.
     */
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
