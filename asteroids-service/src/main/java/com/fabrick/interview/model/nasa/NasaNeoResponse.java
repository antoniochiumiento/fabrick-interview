package com.fabrick.interview.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NasaNeoResponse(
        String id,
        String name,
        @JsonProperty("close_approach_data")
        List<CloseApproachData> closeApproachData
) {}
