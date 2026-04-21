package com.neoprep.controller;

import com.neoprep.dto.OnboardingRequest;
import com.neoprep.dto.RoadmapResponseDto;
import com.neoprep.service.RoadmapService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final RoadmapService roadmapService;

    public OnboardingController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @PostMapping("/roadmap")
    public RoadmapResponseDto generateRoadmap(@Valid @RequestBody OnboardingRequest request) {
        return roadmapService.generateRoadmap(request);
    }
}
