package com.fabrick.interview.controller;

import com.fabrick.interview.weather.WeatherApplication;
import com.fabrick.interview.weather.controller.WeatherController;
import com.fabrick.interview.weather.model.Station;
import com.fabrick.interview.weather.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = WeatherApplication.class)
@WebFluxTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    @DisplayName("GET /airports/{id}/stations should return data")
    void getStationsByAirport() {

        Station mockStation = new Station("KAPA", "Centennial", 39.0f, -104.0f);
        given(weatherService.findStationsCloseToAirport("KDEN", 0.5f))
                .willReturn(Flux.just(mockStation));


        webClient.get()
                .uri("/api/fabrick/v1.0/airports/KDEN/stations?closestBy=0.5")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Station.class)
                .hasSize(1)
                .consumeWith(response -> {
                    var stations = response.getResponseBody();
                    assert stations != null;
                    assert stations.size() == 1;
                    assert stations.get(0).getId().equals("KAPA");
                });
    }

    @Test
    @DisplayName("GET /stations/{id}/airports should use default closestBy if missing")
    void getAirportsByStationDefaultParam() {

        given(weatherService.findAirportsCloseToStation("KAPA", 0.0f))
                .willReturn(Flux.empty());


        webClient.get()
                .uri("/api/fabrick/v1.0/stations/KAPA/airports")
                .exchange()
                .expectStatus().isOk();
    }
}