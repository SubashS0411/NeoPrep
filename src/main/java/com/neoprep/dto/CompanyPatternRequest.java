package com.neoprep.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyPatternRequest(
        @NotBlank String company,
        @NotBlank String tier,
        @NotBlank String category,
        @NotBlank String patternDetail,
        @NotNull @Min(1) @Max(100) Integer freshnessScore,
        @NotNull @Min(0) @Max(1) Double sourceConfidence,
        @NotBlank String source
) {}
