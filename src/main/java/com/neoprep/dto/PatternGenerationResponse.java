package com.neoprep.dto;

public record PatternGenerationResponse(
        String company,
        String tier,
        String generatedPractice
) {}
