package com.neoprep.service;

import com.neoprep.dto.OnboardingRequest;
import com.neoprep.dto.StandupDto;
import com.neoprep.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StandupServiceTest {

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private StandupService standupService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void shouldBeIdempotentPerDay() {
        String email = "standup@example.com";
        roadmapService.generateRoadmap(new OnboardingRequest(email, "Tier-2", "Java"));
        Long userId = userProfileRepository.findByEmail(email).orElseThrow().getId();

        StandupDto first = standupService.generateForUser(userId);
        StandupDto second = standupService.generateForUser(userId);

        assertEquals(first.id(), second.id());
    }
}
