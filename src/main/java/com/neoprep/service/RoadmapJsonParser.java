package com.neoprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.dto.RoadmapGeneration;
import com.neoprep.dto.RoadmapGenerationDay;
import com.neoprep.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoadmapJsonParser {

    private final ObjectMapper objectMapper;

    public RoadmapJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RoadmapGeneration parseAndValidate(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            requireText(root, "roadmapName");
            JsonNode daysNode = root.get("days");
            if (daysNode == null || !daysNode.isArray()) {
                throw new BadRequestException("Gemini response must include an array 'days'.");
            }

            List<RoadmapGenerationDay> days = new ArrayList<>();
            for (JsonNode dayNode : daysNode) {
                Integer dayNumber = requireInt(dayNode, "dayNumber");
                String focusArea = requireText(dayNode, "focusArea");
                String primaryTopic = requireText(dayNode, "primaryTopic");
                String actionableTask = requireText(dayNode, "actionableTask");
                String leetcodePattern = dayNode.hasNonNull("leetcodePattern") ? dayNode.get("leetcodePattern").asText() : null;
                days.add(new RoadmapGenerationDay(dayNumber, focusArea, primaryTopic, actionableTask, leetcodePattern));
            }

            if (days.size() != 35) {
                throw new BadRequestException("Roadmap must have exactly 35 days.");
            }

            String tier = root.hasNonNull("targetCompanyTier") ? root.get("targetCompanyTier").asText() : null;
            return new RoadmapGeneration(root.get("roadmapName").asText(), tier, days);
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Invalid Gemini JSON response.");
        }
    }

    private String requireText(JsonNode node, String key) {
        if (!node.hasNonNull(key) || node.get(key).asText().isBlank()) {
            throw new BadRequestException("Missing required field: " + key);
        }
        return node.get(key).asText();
    }

    private Integer requireInt(JsonNode node, String key) {
        if (!node.hasNonNull(key) || !node.get(key).canConvertToInt()) {
            throw new BadRequestException("Missing required integer field: " + key);
        }
        return node.get(key).asInt();
    }
}
