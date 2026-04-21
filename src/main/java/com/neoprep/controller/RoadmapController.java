package com.neoprep.controller;

import com.neoprep.dto.RoadmapDayDto;
import com.neoprep.dto.RoadmapRecalculationRequest;
import com.neoprep.dto.RoadmapRecalculationResponse;
import com.neoprep.dto.RoadmapResponseDto;
import com.neoprep.service.RoadmapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @GetMapping("/{roadmapId}")
    public RoadmapResponseDto getRoadmap(@PathVariable Long roadmapId) {
        return roadmapService.mapRoadmap(roadmapId);
    }

    @GetMapping("/{roadmapId}/days/{dayNumber}")
    public RoadmapDayDto getDay(@PathVariable Long roadmapId, @PathVariable Integer dayNumber) {
        return roadmapService.getDay(roadmapId, dayNumber);
    }

    @PostMapping("/{roadmapId}/days/{dayNumber}/complete")
    public ResponseEntity<Void> markComplete(@PathVariable Long roadmapId, @PathVariable Integer dayNumber) {
        roadmapService.markDayComplete(roadmapId, dayNumber);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roadmapId}/recalculate")
    public RoadmapRecalculationResponse recalculate(@PathVariable Long roadmapId,
                                                    @Valid @RequestBody RoadmapRecalculationRequest request) {
        return roadmapService.recalculateRoadmap(roadmapId, request);
    }
}
