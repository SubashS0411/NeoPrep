package com.neoprep.dto;

public record ReadinessResponseDto(
        Long scoreId,
        String company,
        int scorePercent,
        String topGaps,
        String nextActions
) {}
