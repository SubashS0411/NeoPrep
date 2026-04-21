package com.neoprep.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OnboardingRequest(
        @Email String email,
        @NotBlank String targetCompanyTier,
        @NotBlank String jobDescription
) {}
