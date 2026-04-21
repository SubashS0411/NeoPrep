package com.neoprep.dto;

import java.util.List;

public record RoadmapResponseDto(
        Long roadmapId,
        String roadmapName,
        String targetCompanyTier,
        List<RoadmapDayDto> days
) {}
