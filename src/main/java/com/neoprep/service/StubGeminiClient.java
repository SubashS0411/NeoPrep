package com.neoprep.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StubGeminiClient implements GeminiClient {

    @Override
    public String generateRoadmapJson(String jobDescription, String targetCompanyTier) {
        String days = IntStream.rangeClosed(1, 35)
                .mapToObj(day -> String.format("""
                        {"dayNumber":%d,"focusArea":"%s","primaryTopic":"%s","actionableTask":"%s","leetcodePattern":%s}
                        """,
                        day,
                        day % 2 == 0 ? "SPRING_BOOT" : "DSA",
                        day % 2 == 0 ? "Spring Boot Architecture Drill " + day : "DSA Pattern Drill " + day,
                        day % 2 == 0 ? "Build one layered service and write tests." : "Solve 3 curated LeetCode problems and explain complexity.",
                        day % 2 == 0 ? "null" : "\"Sliding Window\""
                ))
                .collect(Collectors.joining(","));

        return "{" +
                "\"roadmapName\":\"35-Day Placement Sprint\"," +
                "\"targetCompanyTier\":\"" + targetCompanyTier + "\"," +
                "\"days\":[" + days + "]" +
                "}";
    }

    @Override
    public String evaluateCodeJson(String problemName, String codeText, String imageBase64) {
        return "{" +
                "\"timeComplexity\":\"O(n)\"," +
                "\"spaceComplexity\":\"O(1)\"," +
                "\"bugs\":\"No critical bugs detected; verify edge case handling for empty input.\"," +
                "\"optimalSolution\":\"Use two-pointer/sliding window with clear variable names and guard clauses.\"," +
                "\"algorithmPattern\":\"Sliding Window\"" +
                "}";
    }

    @Override
    public String optimizeCodeJson(String problemName, String codeText, String imageBase64) {
        return "{" +
                "\"memoryFeedback\":\"Avoid retaining unused intermediate arrays.\"," +
                "\"complexityFeedback\":\"Replace nested loops with indexed hash lookup where possible.\"," +
                "\"correctnessFeedback\":\"Add null and empty input guards before iteration.\"," +
                "\"readabilityFeedback\":\"Rename variables to intent-based names and split long methods.\"," +
                "\"lineByLineFeedback\":\"L3: add guard clause; L9: remove repeated map lookup; L14: move constant outside loop.\"" +
                "}";
    }

    @Override
    public String generateStandupJson(String targetTier, String primaryTopic, String carryOver) {
        return "{" +
                "\"summary\":\"Master " + primaryTopic + " and finish one timed coding drill today.\"," +
                "\"marketWhy\":\"" + targetTier + " hiring loops currently prioritize production-grade Java + DSA fluency.\"," +
                "\"carryOver\":\"" + (carryOver == null ? "No carry-over tasks." : carryOver) + "\"" +
                "}";
    }

    @Override
    public String analyzeWhiteboardJson(String imageBase64) {
        return "{" +
                "\"analysis\":\"Detected missing cache invalidation path and single-point-of-failure in service node.\"" +
                "}";
    }

    @Override
    public String generateCompanyPracticeJson(String company, String tier, String styleHint, List<String> patterns) {
        String joined = patterns.isEmpty() ? "core edge-cases" : String.join("; ", patterns);
        return "{" +
                "\"practice\":\"Build a " + company + "-style " + tier + " challenge focusing on " + styleHint + " with patterns: " + joined + ".\"" +
                "}";
    }
}
