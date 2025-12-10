package com.fabrick.interview.weather.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) representing a Weather Observation Station.
 * <p>
 * This class handles the mapping between the external Aviation Weather API format
 * and the internal JSON structure required by the Fabrick API specifications.
 * </p>
 * <p>
 * Key features:
 * <ul>
 * <li><b>Robust Input:</b> Uses {@link JsonAlias} to accept various field names from the external source (e.g., "station_id", "icaoId").</li>
 * <li><b>Compliant Output:</b> Uses {@link JsonProperty} to ensure the JSON response matches the specific keys required by the assignment (e.g., outputting "site" instead of "name").</li>
 * </ul>
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    /**
     * The unique ICAO identifier for the station (e.g., "KAFF").
     * Accepts multiple input keys to ensure compatibility with different API versions.
     */
    @JsonProperty("id")
    @JsonAlias({"icaoId", "station_id", "id", "siteId"})
    private String id;

    /**
     * The human-readable name or location of the station site.
     * <p>
     * <b>Note:</b> According to the project requirements, this field is serialized
     * to JSON with the key <b>"site"</b> (not "name").
     * </p>
     */
    @JsonProperty("site")
    @JsonAlias({"site", "name", "siteName"})
    private String name;

    /**
     * The state or region code where the station is located (e.g., "CO").
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
     * The latitude coordinate of the station.
     */
    @JsonProperty("latitude")
    @JsonAlias({"lat", "latitude"})
    private double latitude;

    /**
     * The longitude coordinate of the station.
     */
    @JsonProperty("longitude")
    @JsonAlias({"lon", "longitude"})
    private double longitude;

    /**
     * The elevation of the station in meters.
     * Use of the wrapper class {@link Double} allows for null values if data is missing.
     */
    @JsonProperty("elevation")
    @JsonAlias({"elev", "elevation", "elevation_m"})
    private Double elevation;

    /**
     * Default no-args constructor required by Jackson for deserialization.
     */
    public Station() {}

    /**
     * Full constructor for manual instantiation (e.g., during unit testing).
     *
     * @param id        ICAO ID.
     * @param name      Site name.
     * @param state     State/Region.
     * @param country   Country code.
     * @param latitude  Latitude.
     * @param longitude Longitude.
     * @param elevation Elevation in meters.
     */
    public Station(String id, String name, String state, String country, double latitude, double longitude, Double elevation) {
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