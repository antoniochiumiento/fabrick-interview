package com.fabrick.interview.weather.client;

import com.fabrick.interview.weather.model.Airport;
import com.fabrick.interview.weather.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AviationApiClient {

    private static final Logger logger = LoggerFactory.getLogger(AviationApiClient.class);
    private final WebClient webClient;

    public AviationApiClient(WebClient.Builder webClientBuilder,
                             @Value("${external.aviation.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Cacheable("stations")
    public Flux<Station> getStationsInBox(String bbox) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stationinfo")
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(Station.class)
                .doOnSubscribe(s -> logger.info("Cache MISS - Fetching Stations bbox: {}", bbox))
                .doOnNext(s -> logger.debug("CLIENT: Ricevuta stazione: {}", s.getId()))
                .cache();
    }

    @Cacheable("airports")
    public Flux<Airport> getAirportsInBox(String bbox) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(Airport.class)
                .doOnSubscribe(s -> logger.info("Cache MISS - Fetching Airports bbox: {}", bbox))
                .doOnNext(a -> logger.debug("CLIENT: Ricevuto aeroporto: {}", a.getId()))
                .cache();
    }

    public Mono<Station> getStationMetadata(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("ids", id)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(Station.class)
                .next() // Prende il primo elemento della lista
                .doOnError(e -> logger.error("CLIENT ERROR su metadata: ", e));
    }
}