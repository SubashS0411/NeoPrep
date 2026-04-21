package com.neoprep.controller;

import com.neoprep.dto.ProgressResponseDto;
import com.neoprep.service.ProgressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/{userId}")
    public ProgressResponseDto getProgress(@PathVariable Long userId) {
        return progressService.getProgress(userId);
    }
}
