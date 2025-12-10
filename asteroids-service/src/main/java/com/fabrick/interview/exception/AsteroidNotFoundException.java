package com.fabrick.interview.exception;

public class AsteroidNotFoundException extends RuntimeException {
    public AsteroidNotFoundException(String asteroidId) {
        super("Asteroid with ID [" + asteroidId + "] not found in NASA database.");
    }
}
