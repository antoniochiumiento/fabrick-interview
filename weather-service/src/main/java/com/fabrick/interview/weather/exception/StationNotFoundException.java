package com.fabrick.interview.weather.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(String stationId) {
        super("Station with ID [" + stationId + "] not found.");
    }
}
