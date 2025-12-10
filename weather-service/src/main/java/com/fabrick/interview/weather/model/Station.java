package com.fabrick.interview.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
    private final String id;
    private final String name;
    private final float latitude;
    private final float longitude;

    public Station(
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