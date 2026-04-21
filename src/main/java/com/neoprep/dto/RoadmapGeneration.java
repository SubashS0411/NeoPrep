package com.neoprep.dto;

import java.util.List;

public record RoadmapGeneration(
        String roadmapName,
        String targetCompanyTier,
        List<RoadmapGenerationDay> days
) {}
