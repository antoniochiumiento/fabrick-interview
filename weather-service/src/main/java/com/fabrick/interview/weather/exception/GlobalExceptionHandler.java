package com.fabrick.interview.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AviationServiceException.class)
    public ProblemDetail handleAviationError(AviationServiceException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
        problem.setTitle("Aviation Weather Service Unavailable");
        problem.setType(URI.create("https://api.fabrick.com/errors/external-service-error"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public ProblemDetail handleAirportNotFound(AirportNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("Airport Not Found");
        problem.setType(URI.create("https://api.fabrick.com/errors/airport-not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ProblemDetail handleStationNotFound(StationNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("Station Not Found");
        problem.setType(URI.create("https://api.fabrick.com/errors/station-not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}