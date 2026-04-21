package com.neoprep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OutreachRequest(
        @NotNull Long userId,
        @NotBlank String company,
        @NotBlank String role,
        @NotBlank String channel
) {}
