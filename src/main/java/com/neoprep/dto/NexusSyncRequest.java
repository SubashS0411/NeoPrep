package com.neoprep.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NexusSyncRequest(
        @NotNull Long userId,
        @NotNull Long roadmapId,
        List<Integer> missedDays,
        List<String> weakTopics
) {}
