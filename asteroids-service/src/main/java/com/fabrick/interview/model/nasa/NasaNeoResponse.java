package com.fabrick.interview.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NasaNeoResponse(
        /**
         * Data Transfer Object (DTO) representing a Near Earth Object (NEO) retrieved from the NASA NeoWs API.
         * <p>
         * This record captures a specific subset of the full JSON response, focusing on the asteroid's identity
         * and its list of close approach events, which are required to calculate its path across the solar system.
         * </p>
         *
         * @param id                The unique SPK-ID of the asteroid.
         * @param name              The name of the asteroid (e.g., "(2015 HZ1)").
         * @param closeApproachData The list of close approach events, mapped from the JSON property {@code "close_approach_data"}.
         */
        String id,
        String name,
        @JsonProperty("close_approach_data")
        List<CloseApproachData> closeApproachData
) {}
