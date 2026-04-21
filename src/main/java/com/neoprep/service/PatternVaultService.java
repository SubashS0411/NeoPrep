package com.neoprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.domain.CompanyPattern;
import com.neoprep.dto.*;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.CompanyPatternRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatternVaultService {

    private final CompanyPatternRepository companyPatternRepository;
    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    public PatternVaultService(CompanyPatternRepository companyPatternRepository,
                               GeminiClient geminiClient,
                               ObjectMapper objectMapper) {
        this.companyPatternRepository = companyPatternRepository;
        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    public List<CompanyPatternDto> list() {
        return companyPatternRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public CompanyPatternDto create(CompanyPatternRequest request) {
        CompanyPattern pattern = new CompanyPattern();
        apply(request, pattern);
        return toDto(companyPatternRepository.save(pattern));
    }

    @Transactional
    public CompanyPatternDto update(Long id, CompanyPatternRequest request) {
        CompanyPattern pattern = companyPatternRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pattern not found"));
        apply(request, pattern);
        return toDto(companyPatternRepository.save(pattern));
    }

    @Transactional
    public void delete(Long id) {
        if (!companyPatternRepository.existsById(id)) {
            throw new NotFoundException("Pattern not found");
        }
        companyPatternRepository.deleteById(id);
    }

    @Transactional
    public List<CompanyPatternDto> ingest(List<CompanyPatternRequest> requests) {
        return requests.stream().map(this::create).toList();
    }

    public PatternGenerationResponse generatePractice(PatternGenerationRequest request) {
        List<CompanyPattern> patterns = companyPatternRepository
                .findByCompanyIgnoreCaseAndTierIgnoreCaseOrderByFreshnessScoreDesc(request.company(), request.tier());

        String generated;
        try {
            String json = geminiClient.generateCompanyPracticeJson(
                    request.company(),
                    request.tier(),
                    request.styleHint(),
                    patterns.stream().map(CompanyPattern::getPatternDetail).limit(5).toList()
            );
            JsonNode root = objectMapper.readTree(json);
            generated = root.hasNonNull("practice") ? root.get("practice").asText() : null;
        } catch (Exception ex) {
            generated = null;
        }

        if (generated == null || generated.isBlank()) {
            generated = "Solve one " + request.tier() + " challenge for " + request.company() +
                    " with style emphasis on " + request.styleHint() + ".";
        }

        return new PatternGenerationResponse(request.company(), request.tier(), generated);
    }

    private void apply(CompanyPatternRequest request, CompanyPattern pattern) {
        pattern.setCompany(request.company());
        pattern.setTier(request.tier());
        pattern.setCategory(request.category());
        pattern.setPatternDetail(request.patternDetail());
        pattern.setFreshnessScore(request.freshnessScore());
        pattern.setSourceConfidence(request.sourceConfidence());
        pattern.setSource(request.source());
    }

    private CompanyPatternDto toDto(CompanyPattern pattern) {
        return new CompanyPatternDto(
                pattern.getId(),
                pattern.getCompany(),
                pattern.getTier(),
                pattern.getCategory(),
                pattern.getPatternDetail(),
                pattern.getFreshnessScore(),
                pattern.getSourceConfidence(),
                pattern.getSource()
        );
    }
}
