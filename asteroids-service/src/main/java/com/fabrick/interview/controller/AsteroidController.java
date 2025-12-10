package com.fabrick.interview.controller;

import com.fabrick.interview.model.AsteroidPath;
import com.fabrick.interview.service.AsteroidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * REST Controller responsible for exposing APIs related to Asteroid analysis.
 * <p>
 * This controller serves as the entry point for the "Asteroids Path" task (Task 1).
 * It handles HTTP requests, validates/defaults input parameters, and delegates business logic
 * to the {@link AsteroidService}.
 * </p>
 * <p>
 * Base Path: {@code /api/fabrick/v1.0/asteroids}
 * </p>
 */
@RestController
@RequestMapping("/api/fabrick/v1.0/asteroids")
public class AsteroidController {

    private static final Logger logger = LoggerFactory.getLogger(AsteroidController.class);

    private final AsteroidService asteroidService;

    /**
     * Dependency Injection constructor.
     *
     * @param asteroidService The service containing the business logic for asteroid path calculations.
     */
    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    /**
     * Retrieves the paths (migrations between planets) of a specific asteroid within a given date range.
     * <p>
     * This endpoint analyzes the asteroid's close approach data to determine when it moves from orbiting
     * one planetary body to another.
     * </p>
     *
     * <h3>Default Behavior for Optional Parameters:</h3>
     * <ul>
     * <li>If {@code fromDate} is omitted, it defaults to <b>100 years ago</b> from today.</li>
     * <li>If {@code toDate} is omitted, it defaults to <b>today</b>.</li>
     * </ul>
     *
     * @param asteroidId The unique NASA SPK-ID of the asteroid (e.g., "3542519").
     * @param fromDate   (Optional) The start date of the analysis range (Format: YYYY-MM-DD).
     * @param toDate     (Optional) The end date of the analysis range (Format: YYYY-MM-DD).
     * @return A {@link Flux} emitting {@link AsteroidPath} objects describing the asteroid's journey.
     */
    @Operation(
            summary = "Calculate Asteroid Paths",
            description = "Analyzes the movement of an asteroid between planets within a specific date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paths calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Asteroid ID not found in NASA database"),
            @ApiResponse(responseCode = "502", description = "External NASA Service unavailable")
    })
    @GetMapping("/{asteroidId}/paths")
    public Flux<AsteroidPath> getAsteroidPaths(
            @PathVariable String asteroidId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
    ) {
        // Requirements: Defaults are "now-100years" and "now" if params are missing.
        LocalDate effectiveFrom = (fromDate != null) ? fromDate : LocalDate.now().minusYears(100);
        LocalDate effectiveTo = (toDate != null) ? toDate : LocalDate.now();

        logger.info("REST Request - Get Paths for AsteroidID: [{}], Range: [{} to {}]",
                asteroidId, effectiveFrom, effectiveTo);

        return asteroidService.getAsteroidPath(asteroidId, effectiveFrom, effectiveTo);
    }


}
