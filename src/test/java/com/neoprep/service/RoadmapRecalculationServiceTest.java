package com.neoprep.service;

import com.neoprep.dto.OnboardingRequest;
import com.neoprep.dto.RoadmapRecalculationRequest;
import com.neoprep.dto.RoadmapResponseDto;
import com.neoprep.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RoadmapRecalculationServiceTest {

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void shouldRecalculateRoadmapWithReasons() {
        String email = "recalc@example.com";
        RoadmapResponseDto roadmap = roadmapService.generateRoadmap(
                new OnboardingRequest(email, "Tier-1", "Spring Boot + DSA")
        );
        Long userId = userProfileRepository.findByEmail(email).orElseThrow().getId();

        var response = roadmapService.recalculateRoadmap(
                roadmap.roadmapId(),
                new RoadmapRecalculationRequest(userId, java.util.List.of(2, 4), java.util.List.of("Spring Boot Security"))
        );

        assertTrue(response.changedDays() > 0);
        assertTrue(!response.reasons().isEmpty());
    }
}
