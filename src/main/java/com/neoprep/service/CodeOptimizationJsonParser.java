package com.neoprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.dto.CodeOptimizationNormalized;
import com.neoprep.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class CodeOptimizationJsonParser {

    private final ObjectMapper objectMapper;

    public CodeOptimizationJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CodeOptimizationNormalized parseAndValidate(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return new CodeOptimizationNormalized(
                    require(root, "memoryFeedback"),
                    require(root, "complexityFeedback"),
                    require(root, "correctnessFeedback"),
                    require(root, "readabilityFeedback"),
                    require(root, "lineByLineFeedback")
            );
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Invalid optimizer JSON response.");
        }
    }

    private String require(JsonNode node, String key) {
        if (!node.hasNonNull(key) || node.get(key).asText().isBlank()) {
            throw new BadRequestException("Missing field in optimizer response: " + key);
        }
        return node.get(key).asText();
    }
}
