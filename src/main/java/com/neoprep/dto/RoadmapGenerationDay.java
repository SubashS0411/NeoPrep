package com.neoprep.dto;

public record RoadmapGenerationDay(
        Integer dayNumber,
        String focusArea,
        String primaryTopic,
        String actionableTask,
        String leetcodePattern
) {}
