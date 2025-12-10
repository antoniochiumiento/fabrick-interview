package com.fabrick.interview.service.impl;

import com.fabrick.interview.weather.client.AviationApiClient;
import com.fabrick.interview.weather.exception.AirportNotFoundException;
import com.fabrick.interview.weather.model.Station;
import com.fabrick.interview.weather.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private AviationApiClient apiClient;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    @DisplayName("Should calculate correct BBOX and return stations")
    void shouldFindStationsCloseToAirport() {

        Station mockAirportMetadata = new Station("KDEN", "Denver Intl", "CO", "US", 40.0, -100.0, 1600.0);

        Station resultStation = new Station("KAPA", "Centennial", "CO", "US", 40.1, -100.1, 1700.0);

        when(apiClient.getStationMetadata("KDEN")).thenReturn(Mono.just(mockAirportMetadata));


        String expectedBbox = "-101.0000,39.0000,-99.0000,41.0000";

        when(apiClient.getStationsInBox(expectedBbox)).thenReturn(Flux.just(resultStation));


        StepVerifier.create(weatherService.findStationsCloseToAirport("KDEN", 1.0))
                .expectNextMatches(station -> station.getId().equals("KAPA"))
                .verifyComplete();


        verify(apiClient).getStationsInBox(eq(expectedBbox));
    }

    @Test
    @DisplayName("Should handle missing airport metadata gracefully")
    void shouldHandleMissingMetadata() {

        when(apiClient.getStationMetadata("INVALID")).thenReturn(Mono.empty());

        StepVerifier.create(weatherService.findStationsCloseToAirport("INVALID", 1.0))
                .expectError(AirportNotFoundException.class)
                .verify();
    }
}