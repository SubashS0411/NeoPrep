package com.neoprep.dto;

public record CodeEvaluationNormalized(
        String timeComplexity,
        String spaceComplexity,
        String bugs,
        String optimalSolution,
        String algorithmPattern
) {}
