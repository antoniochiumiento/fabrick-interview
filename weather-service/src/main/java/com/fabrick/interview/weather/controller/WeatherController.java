package com.fabrick.interview.weather.controller;

import com.fabrick.interview.weather.model.Airport;
import com.fabrick.interview.weather.model.Station;
import com.fabrick.interview.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/fabrick/v1.0")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/airports/{airportId}/stations")
    public Flux<Station> getStationsByAirport(
            @PathVariable("airportId") String airportId,
            @RequestParam(value = "closestBy", defaultValue = "0.0") float closestBy) {

        logger.info(">>> CONTROLLER: Richiesta ricevuta. Airport: {}, Range: {}", airportId, closestBy);
        return weatherService.findStationsCloseToAirport(airportId, closestBy)
                .doOnComplete(() -> logger.info("<<< CONTROLLER: Risposta inviata al client."))
                .doOnError(e -> logger.error("!!! CONTROLLER ERROR: ", e));
    }

    @GetMapping("/stations/{stationId}/airports")
    public Flux<Airport> getAirportsByStation(
            @PathVariable("stationId") String stationId,
            @RequestParam(value = "closestBy", defaultValue = "0.0") float closestBy) {

        logger.info(">>> CONTROLLER: Richiesta ricevuta. Station: {}, Range: {}", stationId, closestBy);
        return weatherService.findAirportsCloseToStation(stationId, closestBy);
    }
}