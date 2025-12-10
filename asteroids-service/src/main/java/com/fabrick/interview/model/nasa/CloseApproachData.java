package com.fabrick.interview.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloseApproachData(
        @JsonProperty("close_approach_date")
        String closeApproachDate,

        @JsonProperty("orbiting_body")
        String orbitingBody
) {}
