package com.fabrick.interview.weather.service;

import com.fabrick.interview.weather.model.Airport;
import com.fabrick.interview.weather.model.Station;
import reactor.core.publisher.Flux;

public interface WeatherService {

    Flux<Station> findStationsCloseToAirport(String airportId, float closestBy);
    Flux<Airport> findAirportsCloseToStation(String stationId, float closestBy);

}
