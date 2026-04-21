package com.neoprep.dto;

import jakarta.validation.constraints.NotBlank;

public record PatternGenerationRequest(
        @NotBlank String company,
        @NotBlank String tier,
        @NotBlank String styleHint
) {}
