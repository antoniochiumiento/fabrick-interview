package com.fabrick.interview.weather.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {
    @JsonAlias({"icaoId", "station_id", "id"})
    private final String id;

    @JsonAlias({"name", "site"})
    private final String name;

    @JsonAlias({"lat", "latitude"})
    private final float latitude;

    @JsonAlias({"lon", "longitude"})
    private final float longitude;

    public Airport(
            @JsonProperty("station_id") String id,
            @JsonProperty("site") String name,
            @JsonProperty("latitude") float latitude,
            @JsonProperty("longitude") float longitude) {
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