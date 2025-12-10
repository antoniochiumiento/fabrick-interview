package com.fabrick.interview.controller;

import com.fabrick.interview.model.AsteroidPath;
import com.fabrick.interview.service.AsteroidService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/fabrick/v1.0/asteroids")
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping("/{asteroidId}/paths")
    public Flux<AsteroidPath> getAsteroidPaths(
            @PathVariable String asteroidId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
    ) {
        LocalDate effectiveFrom = (fromDate != null) ? fromDate : LocalDate.now().minusYears(100);
        LocalDate effectiveTo = (toDate != null) ? toDate : LocalDate.now();
        return asteroidService.getAsteroidPath(asteroidId, effectiveFrom, effectiveTo);
    }


}
