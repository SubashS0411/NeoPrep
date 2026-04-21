package com.neoprep.dto;

public record MockInterviewResponse(
        Long sessionId,
        String prompt,
        String followUpGuidance,
        boolean beta
) {}
