package com.neoprep.service;

import java.util.List;

public interface GeminiClient {
    String generateRoadmapJson(String jobDescription, String targetCompanyTier);
    String evaluateCodeJson(String problemName, String codeText, String imageBase64);
    String optimizeCodeJson(String problemName, String codeText, String imageBase64);
    String generateStandupJson(String targetTier, String primaryTopic, String carryOver);
    String analyzeWhiteboardJson(String imageBase64);
    String generateCompanyPracticeJson(String company, String tier, String styleHint, List<String> patterns);
}
