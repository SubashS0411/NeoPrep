package com.neoprep.service;

import org.springframework.stereotype.Component;

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
}
