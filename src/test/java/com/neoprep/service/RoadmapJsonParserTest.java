package com.neoprep.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoadmapJsonParserTest {

    private final RoadmapJsonParser parser = new RoadmapJsonParser(new ObjectMapper());

    @Test
    void shouldParseValid35DayRoadmap() {
        StringBuilder days = new StringBuilder();
        for (int i = 1; i <= 35; i++) {
            if (i > 1) days.append(',');
            days.append("{" +
                    "\"dayNumber\":" + i + "," +
                    "\"focusArea\":\"DSA\"," +
                    "\"primaryTopic\":\"Topic\"," +
                    "\"actionableTask\":\"Task\"" +
                    "}");
        }

        String json = "{" +
                "\"roadmapName\":\"Plan\"," +
                "\"days\":[" + days + "]" +
                "}";

        assertEquals(35, parser.parseAndValidate(json).days().size());
    }

    @Test
    void shouldFailWhenMissingDays() {
        assertThrows(BadRequestException.class,
                () -> parser.parseAndValidate("{\"roadmapName\":\"Plan\",\"days\":[]}"));
    }
}
