package com.neoprep.dto;

public record NexusSyncResponse(
        RoadmapRecalculationResponse recalculation,
        StandupDto standup,
        String achievementTrigger
) {}
