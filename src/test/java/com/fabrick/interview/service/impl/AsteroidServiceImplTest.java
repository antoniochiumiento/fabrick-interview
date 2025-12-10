package com.fabrick.interview.service.impl;

import com.fabrick.interview.client.NasaApiClient;
import com.fabrick.interview.model.nasa.CloseApproachData;
import com.fabrick.interview.model.nasa.NasaNeoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsteroidServiceImplTest {

    @Mock
    private NasaApiClient nasaApiClient;

    @InjectMocks
    private AsteroidServiceImpl asteroidService;

    @Test
    @DisplayName("Should calculate correct paths from raw NASA data")
    void shouldCalculatePathsCorrectly() {
        NasaNeoResponse mockResponse = new NasaNeoResponse(
                "123", "TestAsteroid",
                List.of(
                        new CloseApproachData("2010-01-01", "Earth"),
                        new CloseApproachData("2015-01-01", "Juptr"),
                        new CloseApproachData("2020-01-01", "Mars")
                )
        );

        when(nasaApiClient.getAsteroidData(anyString())).thenReturn(Mono.just(mockResponse));

        StepVerifier.create(asteroidService.getAsteroidPath("123", null, null))
                .expectNextMatches(path ->
                        path.getFromPlanet().equals("Earth") &&
                                path.getToPlanet().equals("Juptr") &&
                                path.getFromDate().equals(LocalDate.of(2010, 1, 1)) &&
                                path.getToDate().equals(LocalDate.of(2015, 1, 1))
                )
                .expectNextMatches(path ->
                        path.getFromPlanet().equals("Juptr") &&
                                path.getToPlanet().equals("Mars") &&
                                path.getFromDate().equals(LocalDate.of(2015, 1, 1)) &&
                                path.getToDate().equals(LocalDate.of(2020, 1, 1))
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Should filter paths based on date range")
    void shouldFilterPathsByDate() {
        NasaNeoResponse mockResponse = new NasaNeoResponse(
                "123", "TestAsteroid",
                List.of(
                        new CloseApproachData("2010-01-01", "Earth"),
                        new CloseApproachData("2015-01-01", "Juptr"),
                        new CloseApproachData("2020-01-01", "Mars")
                )
        );
        when(nasaApiClient.getAsteroidData(anyString())).thenReturn(Mono.just(mockResponse));

        LocalDate fromDate = LocalDate.of(2012, 1, 1);

        StepVerifier.create(asteroidService.getAsteroidPath("123", fromDate, null))
                .expectNextMatches(path -> path.getFromPlanet().equals("Juptr"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should handle empty data gracefully")
    void shouldHandleEmptyData() {
        NasaNeoResponse mockResponse = new NasaNeoResponse("123", "Empty", List.of());
        when(nasaApiClient.getAsteroidData(anyString())).thenReturn(Mono.just(mockResponse));

        StepVerifier.create(asteroidService.getAsteroidPath("123", null, null))
                .verifyComplete();
    }
}