package com.neoprep.controller;

import com.neoprep.dto.CodeEvaluationResponseDto;
import com.neoprep.service.CodeEvaluationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/submissions")
public class EvaluationController {

    private final CodeEvaluationService codeEvaluationService;

    public EvaluationController(CodeEvaluationService codeEvaluationService) {
        this.codeEvaluationService = codeEvaluationService;
    }

    @PostMapping(value = "/evaluate", consumes = {"multipart/form-data"})
    public CodeEvaluationResponseDto evaluate(@RequestParam Long userId,
                                              @RequestParam(required = false) Long dayPlanId,
                                              @RequestParam String problemName,
                                              @RequestParam(required = false) String codeText,
                                              @RequestPart(required = false) MultipartFile image) throws Exception {
        byte[] imageBytes = image != null ? image.getBytes() : null;
        return codeEvaluationService.evaluate(userId, dayPlanId, problemName, codeText, imageBytes);
    }
}
