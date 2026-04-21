package com.neoprep.dto;

public record CodeOptimizeResponseDto(
        Long attemptId,
        String memoryFeedback,
        String complexityFeedback,
        String correctnessFeedback,
        String readabilityFeedback,
        String lineByLineFeedback,
        String improvementDelta
) {}
