package com.neoprep.dto;

import java.util.List;

public record RoadmapRecalculationResponse(
        Long roadmapId,
        int changedDays,
        List<RecalculationReasonDto> reasons,
        RoadmapResponseDto roadmap
) {}
