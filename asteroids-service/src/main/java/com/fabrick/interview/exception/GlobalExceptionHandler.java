package com.fabrick.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AsteroidNotFoundException.class)
    public ProblemDetail handleNotFound(AsteroidNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("Asteroid Not Found");
        problem.setType(URI.create("https://api.fabrick.com/errors/not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(NasaServiceException.class)
    public ProblemDetail handleNasaError(NasaServiceException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
        problem.setTitle("NASA Service Unavailable");
        problem.setType(URI.create("https://api.fabrick.com/errors/external-service-error"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}