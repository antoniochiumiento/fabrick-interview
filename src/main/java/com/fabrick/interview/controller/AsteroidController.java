package com.fabrick.interview.controller;

import com.fabrick.interview.model.AsteroidPath;
import com.fabrick.interview.service.AsteroidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/fabrick/v1.0/asteroids")
public class AsteroidController {

    private static final Logger logger = LoggerFactory.getLogger(AsteroidController.class);

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

        logger.info("REST Request - Get Paths for AsteroidID: [{}], Range: [{} to {}]",
                asteroidId, effectiveFrom, effectiveTo);

        return asteroidService.getAsteroidPath(asteroidId, effectiveFrom, effectiveTo);
    }


}
