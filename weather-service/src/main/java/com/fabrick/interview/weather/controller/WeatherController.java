package com.fabrick.interview.weather.controller;

import com.fabrick.interview.weather.model.Airport;
import com.fabrick.interview.weather.model.Station;
import com.fabrick.interview.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * REST Controller responsible for exposing APIs related to aviation weather entities.
 * <p>
 * This controller serves as the entry point for Task 2 (Airports and Stations).
 * It handles incoming HTTP requests to find connections between Airports and Weather Stations
 * based on geographic proximity.
 * </p>
 * <p>
 * The logic relies on calculating a geographic "Bounding Box" around the source entity.
 * </p>
 */
@RestController
@RequestMapping("/api/fabrick/v1.0")
@Tag(name = "Weather", description = "Operations for Airports and Weather Stations")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    /**
     * Dependency Injection constructor.
     *
     * @param weatherService The service containing business logic for geographic lookups.
     */
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Retrieves all weather observation stations located near a specific airport.
     * <p>
     * The proximity is determined by a Bounding Box calculated around the airport's coordinates.
     * The size of the box is modified by the {@code closestBy} parameter.
     * </p>
     *
     * @param airportId The ICAO code of the airport (e.g., "KDEN" for Denver International).
     * @param closestBy A modifier for the bounding box size (in degrees). Default is 0.0.
     * Example: If lat=10 and closestBy=1, the search range is [9, 11].
     * @return A {@link Flux} emitting {@link Station} objects found within the calculated area.
     */
    @Operation(summary = "Find Stations near Airport", description = "Retrieves weather stations within a calculated bounding box around a specific airport.")
    @GetMapping("/airports/{airportId}/stations")
    public Flux<Station> getStationsByAirport(
            @Parameter(description = "ICAO Code of the Airport (e.g., KDEN)", example = "KDEN")
            @PathVariable("airportId") String airportId,

            @Parameter(description = "Range modifier for the bounding box. Default: 0.0")
            @RequestParam(value = "closestBy", defaultValue = "0.0") double closestBy) {

        logger.info(">>> CONTROLLER: Richiesta ricevuta. Airport: {}, Range: {}", airportId, closestBy);
        return weatherService.findStationsCloseToAirport(airportId, closestBy);
    }

    /**
     * Retrieves all airports located near a specific weather observation station.
     * <p>
     * Similar to the airport search, this looks up the station's coordinates and searches
     * for airports within the Bounding Box modified by {@code closestBy}.
     * </p>
     *
     * @param stationId The ICAO code or ID of the weather station (e.g., "KAPA").
     * @param closestBy A modifier for the bounding box size (in degrees). Default is 0.0.
     * @return A {@link Flux} emitting {@link Airport} objects found within the calculated area.
     */
    @Operation(summary = "Find Airports near Station", description = "Retrieves airports within a calculated bounding box around a specific weather station.")
    @GetMapping("/stations/{stationId}/airports")
    public Flux<Airport> getAirportsByStation(
            @Parameter(description = "ICAO Code of the Station", example = "KAPA")
            @PathVariable("stationId") String stationId,

            @Parameter(description = "Range modifier for the bounding box. Default: 0.0")
            @RequestParam(value = "closestBy", defaultValue = "0.0") double closestBy) {

        logger.info(">>> CONTROLLER: Richiesta ricevuta. Station: {}, Range: {}", stationId, closestBy);
        return weatherService.findAirportsCloseToStation(stationId, closestBy);
    }
}