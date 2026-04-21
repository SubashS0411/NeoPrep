package com.neoprep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MockInterviewRequest(
        @NotNull Long userId,
        @NotBlank String mode,
        @NotBlank String topic
) {}
