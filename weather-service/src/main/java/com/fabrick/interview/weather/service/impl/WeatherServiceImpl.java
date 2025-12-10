package com.fabrick.interview.weather.service.impl;

import com.fabrick.interview.weather.client.AviationApiClient;
import com.fabrick.interview.weather.exception.AirportNotFoundException;
import com.fabrick.interview.weather.exception.StationNotFoundException;
import com.fabrick.interview.weather.model.Airport;
import com.fabrick.interview.weather.model.Station;
import com.fabrick.interview.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private final AviationApiClient apiClient;

    public WeatherServiceImpl(AviationApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Finds observation stations close to a given airport.
     * Throws AirportNotFoundException if the airport ID is invalid.
     */
    @Override
    public Flux<Station> findStationsCloseToAirport(String airportId, double closestBy) {
        logger.debug("SERVICE: Cerco metadati per aeroporto {}", airportId);

        return apiClient.getStationMetadata(airportId)
                .switchIfEmpty(Mono.error(new AirportNotFoundException(airportId)))
                .flatMapMany(meta -> {
                    logger.info("SERVICE: Trovato {}. Coord: Lat={}, Lon={}",
                            meta.getName(), meta.getLatitude(), meta.getLongitude());

                    String bbox = calculateBBox(meta.getLatitude(), meta.getLongitude(), closestBy);
                    logger.info("SERVICE: Cerco stazioni nel BBOX: {}", bbox);

                    return apiClient.getStationsInBox(bbox);
                });
    }

    /**
     * Finds airports close to a given observation station.
     * Throws StationNotFoundException if the station ID is invalid.
     */
    @Override
    public Flux<Airport> findAirportsCloseToStation(String stationId, double closestBy) {
        return apiClient.getStationMetadata(stationId)
                .switchIfEmpty(Mono.error(new StationNotFoundException(stationId)))
                .flatMapMany(meta -> {
                    logger.info("SERVICE: Trovato {}. Coord: Lat={}, Lon={}",
                            meta.getName(), meta.getLatitude(), meta.getLongitude());

                    String bbox = calculateBBox(meta.getLatitude(), meta.getLongitude(), closestBy);
                    return apiClient.getAirportsInBox(bbox);
                });
    }

    private String calculateBBox(double lat, double lon, double delta) {
        double minLon = lon - delta;
        double minLat = lat - delta;
        double maxLon = lon + delta;
        double maxLat = lat + delta;

        return String.format(Locale.US, "%.4f,%.4f,%.4f,%.4f", minLon, minLat, maxLon, maxLat);
    }
}