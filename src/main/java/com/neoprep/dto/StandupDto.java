package com.neoprep.dto;

import java.time.LocalDate;

public record StandupDto(
        Long id,
        LocalDate standupDate,
        String summary,
        String marketWhy,
        String carryOver
) {}
