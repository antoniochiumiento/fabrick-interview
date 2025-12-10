package com.fabrick.interview.service;

import com.fabrick.interview.model.AsteroidPath;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AsteroidService {

    Flux<AsteroidPath> getAsteroidPath(String asteroidId, LocalDate fromDate, LocalDate toDate);
}
