package com.neoprep.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoadmapRecalculationRequest(
        @NotNull Long userId,
        List<Integer> missedDays,
        List<String> weakTopics
) {}
