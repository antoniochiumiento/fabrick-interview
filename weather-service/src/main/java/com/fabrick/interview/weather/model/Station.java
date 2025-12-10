package com.fabrick.interview.weather.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    @JsonProperty("id")
    @JsonAlias({"icaoId", "station_id", "id", "siteId"})
    private String id;

    @JsonProperty("site")
    @JsonAlias({"site", "name", "siteName"})
    private String name;

    @JsonProperty("state")
    @JsonAlias({"state", "region"})
    private String state;

    @JsonProperty("country")
    @JsonAlias({"country"})
    private String country;

    @JsonProperty("latitude")
    @JsonAlias({"lat", "latitude"})
    private double latitude;

    @JsonProperty("longitude")
    @JsonAlias({"lon", "longitude"})
    private double longitude;

    @JsonProperty("elevation")
    @JsonAlias({"elev", "elevation", "elevation_m"})
    private Double elevation;

    public Station() {}

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