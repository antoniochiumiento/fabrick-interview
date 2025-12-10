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

@RestController
@RequestMapping("/api/fabrick/v1.0")
@Tag(name = "Weather", description = "Operations for Airports and Weather Stations")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

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