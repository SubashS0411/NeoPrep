package com.neoprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.dto.CodeEvaluationNormalized;
import com.neoprep.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class CodeEvaluationJsonParser {

    private final ObjectMapper objectMapper;

    public CodeEvaluationJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CodeEvaluationNormalized parseAndValidate(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return new CodeEvaluationNormalized(
                    require(root, "timeComplexity"),
                    require(root, "spaceComplexity"),
                    require(root, "bugs"),
                    require(root, "optimalSolution"),
                    require(root, "algorithmPattern")
            );
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Invalid evaluator JSON response.");
        }
    }

    private String require(JsonNode node, String key) {
        if (!node.hasNonNull(key) || node.get(key).asText().isBlank()) {
            throw new BadRequestException("Missing field in evaluator response: " + key);
        }
        return node.get(key).asText();
    }
}
