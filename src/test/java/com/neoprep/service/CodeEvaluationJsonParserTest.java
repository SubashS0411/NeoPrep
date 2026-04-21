package com.neoprep.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeEvaluationJsonParserTest {

    private final CodeEvaluationJsonParser parser = new CodeEvaluationJsonParser(new ObjectMapper());

    @Test
    void shouldNormalizeEvaluatorResponse() {
        String json = "{" +
                "\"timeComplexity\":\"O(n)\"," +
                "\"spaceComplexity\":\"O(1)\"," +
                "\"bugs\":\"none\"," +
                "\"optimalSolution\":\"two pointers\"," +
                "\"algorithmPattern\":\"Sliding Window\"" +
                "}";

        assertEquals("O(n)", parser.parseAndValidate(json).timeComplexity());
    }
}
