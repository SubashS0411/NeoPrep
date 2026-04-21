package com.neoprep.service;

public interface GeminiClient {
    String generateRoadmapJson(String jobDescription, String targetCompanyTier);
    String evaluateCodeJson(String problemName, String codeText, String imageBase64);
}
