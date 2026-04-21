package com.neoprep.dto;

public record RecalculationReasonDto(
        String reason,
        double weight,
        String source
) {}
