package com.neoprep.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeOptimizationJsonParserTest {

    private final CodeOptimizationJsonParser parser = new CodeOptimizationJsonParser(new ObjectMapper());

    @Test
    void shouldParseOptimizerResponse() {
        String json = "{" +
                "\"memoryFeedback\":\"mem\"," +
                "\"complexityFeedback\":\"complex\"," +
                "\"correctnessFeedback\":\"correct\"," +
                "\"readabilityFeedback\":\"readable\"," +
                "\"lineByLineFeedback\":\"line\"" +
                "}";

        assertEquals("mem", parser.parseAndValidate(json).memoryFeedback());
    }
}
