package com.neoprep.dto;

public record CodeOptimizationNormalized(
        String memoryFeedback,
        String complexityFeedback,
        String correctnessFeedback,
        String readabilityFeedback,
        String lineByLineFeedback
) {}
