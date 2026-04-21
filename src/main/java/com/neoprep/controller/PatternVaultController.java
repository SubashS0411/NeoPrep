package com.neoprep.controller;

import com.neoprep.dto.*;
import com.neoprep.service.PatternVaultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pattern-vault")
public class PatternVaultController {

    private final PatternVaultService patternVaultService;

    public PatternVaultController(PatternVaultService patternVaultService) {
        this.patternVaultService = patternVaultService;
    }

    @GetMapping
    public List<CompanyPatternDto> list() {
        return patternVaultService.list();
    }

    @PostMapping
    public CompanyPatternDto create(@Valid @RequestBody CompanyPatternRequest request) {
        return patternVaultService.create(request);
    }

    @PutMapping("/{id}")
    public CompanyPatternDto update(@PathVariable Long id, @Valid @RequestBody CompanyPatternRequest request) {
        return patternVaultService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patternVaultService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ingest")
    public List<CompanyPatternDto> ingest(@Valid @RequestBody List<CompanyPatternRequest> requests) {
        return patternVaultService.ingest(requests);
    }

    @PostMapping("/generate")
    public PatternGenerationResponse generate(@Valid @RequestBody PatternGenerationRequest request) {
        return patternVaultService.generatePractice(request);
    }
}
