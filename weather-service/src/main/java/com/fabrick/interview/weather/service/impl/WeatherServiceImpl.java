package com.fabrick.interview.weather.service.impl;

import com.fabrick.interview.weather.client.AviationApiClient;
import com.fabrick.interview.weather.model.Airport;
import com.fabrick.interview.weather.model.Station;
import com.fabrick.interview.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Locale;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private final AviationApiClient apiClient;

    public WeatherServiceImpl(AviationApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Flux<Station> findStationsCloseToAirport(String airportId, float closestBy) {
        logger.debug("SERVICE: Cerco metadati per aeroporto {}", airportId);

        return apiClient.getStationMetadata(airportId)
                .doOnSuccess(meta -> {
                    if (meta == null) logger.warn("SERVICE: Nessun metadato trovato per {}", airportId);
                    else logger.info("SERVICE: Trovato {}. Coord: Lat={}, Lon={}", meta.getName(), meta.getLatitude(), meta.getLongitude());
                })
                .flatMapMany(meta -> {
                    String bbox = calculateBBox(meta.getLatitude(), meta.getLongitude(), closestBy);
                    logger.info("SERVICE: Cerco stazioni nel BBOX: {}", bbox);
                    return apiClient.getStationsInBox(bbox);
                });
    }

    @Override
    public Flux<Airport> findAirportsCloseToStation(String stationId, float closestBy) {
        return apiClient.getStationMetadata(stationId)
                .flatMapMany(meta -> {
                    String bbox = calculateBBox(meta.getLatitude(), meta.getLongitude(), closestBy);
                    return apiClient.getAirportsInBox(bbox);
                });
    }

    private String calculateBBox(float lat, float lon, float delta) {
        float minLon = lon - delta;
        float minLat = lat - delta;
        float maxLon = lon + delta;
        float maxLat = lat + delta;

        return String.format(Locale.US, "%.4f,%.4f,%.4f,%.4f", minLon, minLat, maxLon, maxLat);
    }
}