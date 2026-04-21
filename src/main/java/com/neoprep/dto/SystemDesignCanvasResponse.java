package com.neoprep.dto;

public record SystemDesignCanvasResponse(
        Long sessionId,
        String jarvisFeedback,
        boolean beta
) {}
