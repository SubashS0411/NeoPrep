package com.neoprep.dto;

public record ProgressResponseDto(
        Long completedDays,
        Integer streak,
        String latestAchievement
) {}
