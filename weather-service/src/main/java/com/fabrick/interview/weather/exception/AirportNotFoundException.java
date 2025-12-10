package com.fabrick.interview.weather.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String airportId) {
        super("Airport with ID [" + airportId + "] not found.");
    }
}
