package com.fabrick.interview.weather.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    // Alias per coprire "station_id" (vecchio), "icaoId" (nuovo), "id"
    @JsonAlias({"icaoId", "station_id", "id", "siteId"})
    private String id;

    @JsonAlias({"name", "site", "siteName"})
    private String name;

    @JsonAlias({"lat", "latitude"})
    private float latitude;

    @JsonAlias({"lon", "longitude"})
    private float longitude;

    public Station() {}

    public Station(String id, String name, float latitude, float longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public float getLatitude() { return latitude; }
    public float getLongitude() { return longitude; }
}