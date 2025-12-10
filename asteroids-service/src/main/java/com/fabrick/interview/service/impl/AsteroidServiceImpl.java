package com.fabrick.interview.service.impl;

import com.fabrick.interview.client.NasaApiClient;
import com.fabrick.interview.model.AsteroidPath;
import com.fabrick.interview.model.nasa.CloseApproachData;
import com.fabrick.interview.model.nasa.NasaNeoResponse;
import com.fabrick.interview.service.AsteroidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of the {@link AsteroidService} that handles the business logic for calculating asteroid paths.
 * <p>
 * This service acts as the orchestrator between the external NASA API client and the controller.
 * Its primary responsibility is to fetch raw close approach data, sort it chronologically,
 * and identify "migration" events where the asteroid moves from orbiting one body to another.
 * </p>
 */
@Service
public class AsteroidServiceImpl implements AsteroidService {

    private static final Logger logger = LoggerFactory.getLogger(AsteroidServiceImpl.class);

    private final NasaApiClient nasaApiClient;

    public AsteroidServiceImpl(NasaApiClient nasaApiClient) {
        this.nasaApiClient = nasaApiClient;
    }

    /**
     * Retrieves and calculates the paths of an asteroid based on its close approach history.
     * <p>
     * The method performs the following reactive flow:
     * <ol>
     * <li>Fetches raw data asynchronously via {@link NasaApiClient}.</li>
     * <li>Processes the raw data synchronously to compute paths (sorting and filtering).</li>
     * <li>Converts the resulting list of paths into a reactive {@link Flux} stream.</li>
     * </ol>
     * </p>
     *
     * @param asteroidId The ID of the asteroid.
     * @param fromDate   Filter start date (inclusive).
     * @param toDate     Filter end date (inclusive).
     * @return A {@link Flux} of {@link AsteroidPath} objects.
     */
    @Override
    public Flux<AsteroidPath> getAsteroidPath(String asteroidId, LocalDate fromDate, LocalDate toDate) {
        return nasaApiClient.getAsteroidData(asteroidId)
                .map(response -> calculatePaths(response, fromDate, toDate))
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * Core business logic that transforms raw NASA data into migration paths.
     * <p>
     * <b>Algorithm details:</b>
     * <ol>
     * <li>Extracts close approach data and maps it to a lightweight {@link SortedEvent} structure.</li>
     * <li>Sorts all events chronologically (NASA data is not guaranteed to be sorted).</li>
     * <li>Iterates through the timeline to detect changes in the {@code orbitingBody}.</li>
     * <li>When the orbiting body changes (e.g., from "Earth" to "Juptr"), a new {@link AsteroidPath} is created.</li>
     * <li>Filters the resulting path against the requested date range.</li>
     * </ol>
     * </p>
     *
     * @param response   The raw response from the NASA API.
     * @param filterFrom The start date for filtering.
     * @param filterTo   The end date for filtering.
     * @return A list of valid {@link AsteroidPath} objects.
     */
    private List<AsteroidPath> calculatePaths(NasaNeoResponse response, LocalDate filterFrom, LocalDate filterTo) {
        List<CloseApproachData> data = response.closeApproachData();
        List<AsteroidPath> paths = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            logger.warn("No close approach data found for asteroid");
            return paths;
        }

        logger.debug("Processing {} raw events from NASA", data.size());

        // 1. Sort events chronologically to reconstruct the timeline
        List<SortedEvent> sortedEvents = data.stream()
                .map(d -> new SortedEvent(
                        LocalDate.parse(d.closeApproachDate()),
                        d.orbitingBody()
                ))
                .sorted(Comparator.comparing(SortedEvent::date))
                .toList();

        if (sortedEvents.isEmpty()) return paths;

        SortedEvent currentStartEvent = sortedEvents.getFirst();

        // 2. Detect orbit changes
        for (int i = 1; i < sortedEvents.size(); i++) {
            SortedEvent nextEvent = sortedEvents.get(i);

            // A path exists only if the orbiting body changes
            if (!nextEvent.body().equals(currentStartEvent.body())) {

                AsteroidPath path = new AsteroidPath(
                        currentStartEvent.body(),
                        nextEvent.body(),
                        currentStartEvent.date(),
                        nextEvent.date()
                );

                if (isWithinRange(path, filterFrom, filterTo)) {
                    paths.add(path);
                }

                // The end of the current path becomes the start of the next potential path
                currentStartEvent = nextEvent;
            }
        }
        logger.info("Calculation finished. Found {} valid paths within date range.", paths.size());
        return paths;
    }

    /**
     * Helper method to validate if a path falls within the requested date range.
     */
    private boolean isWithinRange(AsteroidPath path, LocalDate filterFrom, LocalDate filterTo) {
        if (filterFrom != null && path.getFromDate().isBefore(filterFrom)) return false;
        return filterTo == null || !path.getToDate().isAfter(filterTo);
    }

    /**
     * Internal helper record to streamline the sorting of close approach events.
     */
    private record SortedEvent(LocalDate date, String body) {}
}
