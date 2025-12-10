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

@Service
public class AsteroidServiceImpl implements AsteroidService {

    private static final Logger logger = LoggerFactory.getLogger(AsteroidServiceImpl.class);

    private final NasaApiClient nasaApiClient;

    public AsteroidServiceImpl(NasaApiClient nasaApiClient) {
        this.nasaApiClient = nasaApiClient;
    }

    @Override
    public Flux<AsteroidPath> getAsteroidPath(String asteroidId, LocalDate fromDate, LocalDate toDate) {
        return nasaApiClient.getAsteroidData(asteroidId)
                .map(response -> calculatePaths(response, fromDate, toDate))
                .flatMapMany(Flux::fromIterable);
    }

    private List<AsteroidPath> calculatePaths(NasaNeoResponse response, LocalDate filterFrom, LocalDate filterTo) {
        List<CloseApproachData> data = response.closeApproachData();
        List<AsteroidPath> paths = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            return paths;
        }

        List<SortedEvent> sortedEvents = data.stream()
                .map(d -> new SortedEvent(
                        LocalDate.parse(d.closeApproachDate()),
                        d.orbitingBody()
                ))
                .sorted(Comparator.comparing(SortedEvent::date))
                .toList();

        if (sortedEvents.isEmpty()) return paths;

        SortedEvent currentStartEvent = sortedEvents.get(0);

        for (int i = 1; i < sortedEvents.size(); i++) {
            SortedEvent nextEvent = sortedEvents.get(i);

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

                currentStartEvent = nextEvent;
            }
        }

        return paths;
    }

    private boolean isWithinRange(AsteroidPath path, LocalDate filterFrom, LocalDate filterTo) {
        if (filterFrom != null && path.getFromDate().isBefore(filterFrom)) return false;
        if (filterTo != null && path.getToDate().isAfter(filterTo)) return false;
        return true;
    }

    private record SortedEvent(LocalDate date, String body) {}
}
