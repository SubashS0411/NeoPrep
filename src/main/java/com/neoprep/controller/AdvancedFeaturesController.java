package com.neoprep.controller;

import com.neoprep.dto.*;
import com.neoprep.service.AdvancedFeatureService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/advanced")
public class AdvancedFeaturesController {

    private final AdvancedFeatureService advancedFeatureService;

    public AdvancedFeaturesController(AdvancedFeatureService advancedFeatureService) {
        this.advancedFeatureService = advancedFeatureService;
    }

    @PostMapping(value = "/whiteboard/analyze", consumes = {"multipart/form-data"})
    public WhiteboardAnalysisResponse analyzeWhiteboard(@RequestParam Long userId,
                                                        @RequestPart MultipartFile image) throws Exception {
        return advancedFeatureService.analyzeWhiteboard(userId, image.getBytes());
    }

    @GetMapping("/readiness/{userId}")
    public ReadinessResponseDto readiness(@PathVariable Long userId,
                                          @RequestParam String company) {
        return advancedFeatureService.computeReadiness(userId, company);
    }

    @PostMapping("/outreach/draft")
    public OutreachResponse outreachDraft(@Valid @RequestBody OutreachRequest request) {
        return advancedFeatureService.generateOutreach(request);
    }

    @PostMapping("/interviews/mock/start")
    public MockInterviewResponse startInterview(@Valid @RequestBody MockInterviewRequest request) {
        return advancedFeatureService.startMockInterview(request);
    }

    @PostMapping("/canvas/evaluate")
    public SystemDesignCanvasResponse evaluateCanvas(@Valid @RequestBody SystemDesignCanvasRequest request) {
        return advancedFeatureService.evaluateCanvas(request);
    }
}
