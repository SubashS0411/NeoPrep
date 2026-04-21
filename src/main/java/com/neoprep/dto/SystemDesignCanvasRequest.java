package com.neoprep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SystemDesignCanvasRequest(
        @NotNull Long userId,
        @NotBlank String canvasJson
) {}
