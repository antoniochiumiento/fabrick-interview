package com.fabrick.interview.weather.client;

import com.fabrick.interview.weather.exception.AviationServiceException;
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

/**
 * Reactive client component responsible for interacting with the external Aviation Weather Center API.
 * <p>
 * This client handles HTTP communication using Spring's non-blocking {@link WebClient}.
 * It implements local caching via Caffeine to reduce external calls and latency, and includes
 * custom error handling logic to manage external service failures gracefully.
 * </p>
 */
@Service
public class AviationApiClient {

    private static final Logger logger = LoggerFactory.getLogger(AviationApiClient.class);
    private final WebClient webClient;

    /**
     * Constructs the AviationApiClient with a configured WebClient.
     *
     * @param webClientBuilder The builder to create the WebClient instance.
     * @param baseUrl          The base URL of the Aviation Weather API, injected from properties.
     */
    public AviationApiClient(WebClient.Builder webClientBuilder,
                             @Value("${external.aviation.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Retrieves a list of weather stations within a specific geographic bounding box.
     * <p>
     * This method calls the {@code /stationinfo} endpoint. It incorporates specific error handling:
     * <ul>
     * <li><b>5xx Errors:</b> Throws {@link AviationServiceException} to indicate an external service outage.</li>
     * <li><b>Other Errors (e.g., 404, parsing):</b> Logs the error and returns an empty Flux to allow the application to continue gracefully.</li>
     * </ul>
     * Results are cached in the "stations" cache.
     * </p>
     *
     * @param bbox A string representing the bounding box coordinates (minLon,minLat,maxLon,maxLat).
     * @return A {@link Flux} emitting {@link Station} objects found within the area.
     */
    @Cacheable("stations")
    public Flux<Station> getStationsInBox(String bbox) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stationinfo")
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> Mono.error(new AviationServiceException("External Aviation API is currently unavailable."))
                )
                .bodyToFlux(Station.class)
                .doOnSubscribe(s -> logger.info("Cache MISS - Fetching Stations bbox: {}", bbox))
                .onErrorResume(e -> {
                    // Se è l'errore critico, lo rilanciamo al Controller
                    if (e instanceof AviationServiceException) return Flux.error(e);

                    // Altrimenti (es. errori di parsing o 404 soft), logghiamo e restituiamo vuoto
                    logger.error("Error fetching stations: {}", e.getMessage());
                    return Flux.empty();
                })
                .cache();
    }

    /**
     * Retrieves a list of airports within a specific geographic bounding box.
     * <p>
     * This method calls the {@code /airport} endpoint. Results are cached in the "airports" cache
     * to optimize repeated queries for the same geographic area.
     * </p>
     *
     * @param bbox A string representing the bounding box coordinates (minLon,minLat,maxLon,maxLat).
     * @return A {@link Flux} emitting {@link Airport} objects found within the area.
     */
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

    /**
     * Retrieves metadata (coordinates, name, country) for a specific entity (Airport or Station) by its ID.
     * <p>
     * This method is primarily used to resolve the starting coordinates for the "closestBy" search logic.
     * It uses the {@code /airport} endpoint which supports lookup by ID.
     * </p>
     *
     * @param id The ICAO code or ID of the station/airport (e.g., "KDEN").
     * @return A {@link Mono} containing the {@link Station} details if found, or empty if not.
     */
    public Mono<Station> getStationMetadata(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("ids", id)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(Station.class)
                .next()
                .doOnError(e -> logger.error("CLIENT ERROR su metadata: ", e));
    }
}