package com.neoprep.dto;

public record RoadmapDayDto(
        Integer dayNumber,
        String focusArea,
        String primaryTopic,
        String actionableTask,
        String leetcodePattern,
        boolean completed
) {}
