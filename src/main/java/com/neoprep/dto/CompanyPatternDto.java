package com.neoprep.dto;

public record CompanyPatternDto(
        Long id,
        String company,
        String tier,
        String category,
        String patternDetail,
        Integer freshnessScore,
        Double sourceConfidence,
        String source
) {}
