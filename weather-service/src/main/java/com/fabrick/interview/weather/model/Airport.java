package com.fabrick.interview.weather.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) representing an Airport entity.
 * <p>
 * This class serves a dual purpose:
 * <ol>
 * <li>It maps incoming data from the external Aviation Weather API, handling various field names
 * (e.g., "icaoId", "station_id") via {@link JsonAlias}.</li>
 * <li>It defines the clean JSON structure returned by this microservice's API via {@link JsonProperty}.</li>
 * </ol>
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {

    /**
     * The unique ICAO identifier for the airport (e.g., "KDEN").
     * Maps to multiple possible keys from the external API to ensure robustness.
     */
    @JsonProperty("id")
    @JsonAlias({"icaoId", "station_id", "id", "siteId"})
    private String id;

    /**
     * The full name or description of the airport site.
     * Output as "name" in the JSON response as per project requirements.
     */
    @JsonProperty("name")
    @JsonAlias({"site", "name", "siteName"})
    private String name;

    /**
     * The state or region code where the airport is located (e.g., "CO").
     */
    @JsonProperty("state")
    @JsonAlias({"state", "region"})
    private String state;

    /**
     * The country code (e.g., "US").
     */
    @JsonProperty("country")
    @JsonAlias({"country"})
    private String country;

    /**
     * The latitude coordinate of the airport.
     */
    @JsonProperty("latitude")
    @JsonAlias({"lat", "latitude"})
    private double latitude;

    /**
     * The longitude coordinate of the airport.
     */
    @JsonProperty("longitude")
    @JsonAlias({"lon", "longitude"})
    private double longitude;

    /**
     * The elevation of the airport in meters.
     * Can be null if the external source does not provide it.
     */
    @JsonProperty("elevation")
    @JsonAlias({"elev", "elevation", "elevation_m"})
    private Double elevation;

    /**
     * Default no-args constructor required by Jackson for deserialization.
     */
    public Airport() {}

    /**
     * Full constructor for creating instances manually (e.g., during testing).
     *
     * @param id        ICAO ID.
     * @param name      Site name.
     * @param state     State/Region.
     * @param country   Country code.
     * @param latitude  Latitude.
     * @param longitude Longitude.
     * @param elevation Elevation in meters.
     */
    public Airport(String id, String name, String state, String country, double latitude, double longitude, Double elevation) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Double getElevation() { return elevation; }
}