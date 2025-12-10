package com.fabrick.interview.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloseApproachData(
        /**
         * Represents a specific subset of the "Close Approach Data" from the NASA NeoWs API.
         * <p>
         * This record maps only the fields necessary for calculating the asteroid's path,
         * utilizing Jackson annotations to handle the snake_case JSON keys.
         * </p>
         *
         * @param closeApproachDate The date of the close approach (mapped from JSON {@code "close_approach_date"}).
         * @param orbitingBody      The celestial body the asteroid is orbiting/approaching (mapped from JSON {@code "orbiting_body"}).
         */
        @JsonProperty("close_approach_date")
        String closeApproachDate,

        @JsonProperty("orbiting_body")
        String orbitingBody
) {}
