package com.neoprep.dto;

public record CodeEvaluationResponseDto(
        Long submissionId,
        Long evaluationId,
        String timeComplexity,
        String spaceComplexity,
        String bugs,
        String optimalSolution,
        String algorithmPattern
) {}
